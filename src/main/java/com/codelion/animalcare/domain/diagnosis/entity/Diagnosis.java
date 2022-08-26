package com.codelion.animalcare.domain.diagnosis.entity;

import com.codelion.animalcare.domain.user.entity.Member;
import com.codelion.animalcare.global.common.entity.BaseEntity;
import com.codelion.animalcare.domain.animal.entity.Animal;
import com.codelion.animalcare.domain.doctor.entity.Doctor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Diagnosis extends BaseEntity{

    @Column(nullable = false, length = 30)
    private String title;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

    @Column(nullable = false, length = 20)
    private String cost;

    @Column(nullable = false, length = 45)
    private String diagnosisName;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

//    @Builder
//    private Diagnosis(Long id, LocalDateTime createdAt, String title, String content, String cost, String diagnosisName, Doctor doctor, Member member, Animal animal) {
//        super(id, createdAt);
//        this.title = title;
//        this.content = content;
//        this.cost = cost;
//        this.diagnosisName = diagnosisName;
//        this.doctor = doctor;
//        this.member = member;
//        this.animal = animal;
//    }
}
