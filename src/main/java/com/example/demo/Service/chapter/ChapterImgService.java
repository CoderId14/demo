package com.example.demo.Service.chapter;

import com.example.demo.Repository.chapter.ChapterImgRepo;
import com.example.demo.Repository.chapter.ChapterRepo;
import com.example.demo.Service.role.RoleUtils;
import com.example.demo.api.chapter.request.CreateChapterImgRequest;
import com.example.demo.api.chapter.request.UpdateChapterImgRequest;
import com.example.demo.api.chapter.response.ChapterImgResponse;
import com.example.demo.api.chapter.response.CreateChapterImgResponse;
import com.example.demo.api.chapter.response.ImgChapter;
import com.example.demo.api.chapter.response.UpdateChapterImgResponse;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.chapter.Chapter;
import com.example.demo.entity.chapter.ChapterImg;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class ChapterImgService {
    private final ChapterImgRepo chapterImgRepo;

    private final ChapterRepo chapterRepo;

    private final RoleUtils roleUtils;

    public ChapterImgResponse searchChapterImg(long chapterId, Pageable pageable) {
        chapterRepo.findById(chapterId).orElseThrow(
                () -> new ResourceNotFoundException("chapter", "id", chapterId)
        );
        Page<ChapterImg> chapterImgs = chapterImgRepo.findByChapter_Id(chapterId, pageable);
        List<ChapterImg> content = chapterImgs.getNumberOfElements() == 0 ? Collections.emptyList() : chapterImgs.getContent();

        PagedResponse<ImgChapter> imgChapterList = new PagedResponse<>(getImgChapterList(content), chapterImgs.getNumber(), chapterImgs.getSize(),
                chapterImgs.getTotalElements(), chapterImgs.getTotalPages(), chapterImgs.isLast());
        return getDto(content, imgChapterList);
    }

    public ImgChapter getChapterImg(Predicate predicate) {
        Optional<ChapterImg> chapterImgOptional = chapterImgRepo.findOne(predicate);
        if (chapterImgOptional.isEmpty()) {
            throw new ResourceNotFoundException("chapterImg", "id", 0);
        }
        ChapterImg chapterImg = chapterImgOptional.get();
        return ImgChapter.builder()
                .fileUrl(chapterImg.getFileUrl())
                .imgNumber(chapterImg.getImgNumber())
                .build();
    }

    public CreateChapterImgResponse addChapterImg(CreateChapterImgRequest request, CustomUserDetails currentUser) {
        Chapter chapter = chapterRepo.findById(request.getChapterId()).
                orElseThrow(() -> new ResourceNotFoundException("chapter", "id", request.getChapterId()));
        int lastImgChapterNumber = chapterImgRepo.findLastChapterImgNumber(request.getChapterId());
        roleUtils.checkAuthorization(chapter.getCreatedBy(), currentUser);
        ChapterImg chapterImg = ChapterImg.builder()
                .chapter(chapter)
                .imgNumber(lastImgChapterNumber + 1)
                .fileUrl(request.getFileUrl())
                .build();
        chapterImg = chapterImgRepo.save(chapterImg);
        return CreateChapterImgResponse.builder()
                .chapterId(chapterImg.getChapter().getId())
                .fileUrl(chapterImg.getFileUrl())
                .build();
    }

    public UpdateChapterImgResponse updateChapterImg(Long id, UpdateChapterImgRequest request, CustomUserDetails currentUser) {
        ChapterImg chapterImg = chapterImgRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("chapter", "id", id));
        long chapterId = chapterImg.getChapter().getId();
        Chapter chapter = chapterRepo.findById(chapterId).
                orElseThrow(() -> new ResourceNotFoundException("chapter", "id", chapterId));
        roleUtils.checkAuthorization(chapter.getCreatedBy(), currentUser);

        chapterImg.setFileUrl(request.getFileUrl());
        chapterImgRepo.save(chapterImg);
        return UpdateChapterImgResponse.builder()
                .fileUrl(request.getFileUrl())
                .build();

    }

    public ApiResponse deleteChapterImg(Long id, CustomUserDetails currentUser) {
        ChapterImg chapterImg = chapterImgRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("chapter", "id", id));
        long chapterId = chapterImg.getChapter().getId();
        Chapter chapter = chapterRepo.findById(chapterId).
                orElseThrow(() -> new ResourceNotFoundException("chapter", "id", chapterId));
        roleUtils.checkAuthorization(chapter.getCreatedBy(), currentUser);
        chapterImgRepo.delete(chapterImg);
        return new ApiResponse(Boolean.TRUE, "You successfully deleted chapter", HttpStatus.CREATED);

    }

    private ImgChapter getImgChapter(ChapterImg root) {
        return ImgChapter.builder()
                .fileUrl(root.getFileUrl())
                .imgNumber(root.getImgNumber())
                .build();
    }

    private List<ImgChapter> getImgChapterList(List<ChapterImg> root) {

        List<ImgChapter> imgChapterList = new ArrayList<>();
        root.forEach(img -> imgChapterList.add(getImgChapter(img)));
        return imgChapterList;
    }

    private ChapterImgResponse getDto(List<ChapterImg> root, PagedResponse<ImgChapter> imgPagedResponse) {
        if (root.size() == 0) {
            return ChapterImgResponse.builder().build();
        }

        return ChapterImgResponse.builder()
                .chapterId(root.get(0).getChapter().getId())
                .imgChapterList(imgPagedResponse)
                .build();
    }
}
