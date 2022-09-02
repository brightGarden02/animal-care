package com.codelion.animalcare.domain.doctorqna.service;

import com.codelion.animalcare.domain.doctorqna.dto.request.QuestionSaveRequestDto;
import com.codelion.animalcare.domain.doctorqna.dto.request.QuestionUpdateRequestDto;
import com.codelion.animalcare.domain.doctorqna.dto.response.QuestionResponseDto;
import com.codelion.animalcare.domain.doctorqna.repository.Question;
import com.codelion.animalcare.domain.doctorqna.repository.QuestionRepository;
import com.codelion.animalcare.domain.user.entity.Member;
import com.codelion.animalcare.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;

    private final UserService userService;

    @Transactional
    public Long save(QuestionSaveRequestDto questionSaveRequestDto, Principal principal) {

        Member member = userService.getMember(principal.getName());

        return questionRepository.save(questionSaveRequestDto.toEntity(member)).getId();
    }

    @Transactional
    public Long update(Long id, QuestionUpdateRequestDto questionUpdateRequestDto){
        Question question = questionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("질문이 존재하지 않습니다."));

        question.update(questionUpdateRequestDto.getTitle(), questionUpdateRequestDto.getContent());

        return id;
    }
    @Transactional(readOnly = true)
    public QuestionResponseDto findById(Long id){
        Question entity = questionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("질문이 존재하지 않습니다."));

        return new QuestionResponseDto(entity);

    }

    @Transactional
    public int updateView(Long id) {
        return this.questionRepository.updateView(id);
    }

    @Transactional(readOnly = true)
    public Page<Question> findAll(int page) {

        Pageable pageable = PageRequest.of(page, 10);

        return questionRepository.findAll(pageable);
    }

    @Transactional
    public void delete(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("질문이 존재하지 않습니다."));

        questionRepository.delete(question);
    }

    public boolean questionAuthorized(Long id, Principal principal){
        Question question = questionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("작성된 글이 없습니다."));


        if(question.getMember().getEmail().equals(principal.getName())) {
            return false;
        }

        return true;

    }

    //delete flag



}
