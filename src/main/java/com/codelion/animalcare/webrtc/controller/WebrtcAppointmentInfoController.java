package com.codelion.animalcare.webrtc.controller;

import com.codelion.animalcare.domain.animal.dto.AnimalDto;
import com.codelion.animalcare.domain.animal.service.AnimalService;
import com.codelion.animalcare.domain.appointment.dto.AppointmentDto;
import com.codelion.animalcare.domain.appointment.dto.LoadMyPageDoctorAppointment;
import com.codelion.animalcare.domain.appointment.entity.Appointment;
import com.codelion.animalcare.domain.appointment.service.AppointmentQueryService;
import com.codelion.animalcare.domain.appointment.service.AppointmentService;
import com.codelion.animalcare.domain.diagnosis.dto.FindOneDiagnosis;
import com.codelion.animalcare.domain.diagnosis.service.DiagnosisService;
import com.codelion.animalcare.domain.doctormypage.dto.LoadDoctorMyPageInfo;
import com.codelion.animalcare.domain.hospital.dto.LoadDoctorMyPageHospitalInfoManage;
import com.codelion.animalcare.domain.hospital.service.HospitalService;
import com.codelion.animalcare.domain.user.dto.MemberDto;
import com.codelion.animalcare.domain.user.service.DoctorService;
import com.codelion.animalcare.domain.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usr/mypage/doctor/member-manage/appointments")
@RequiredArgsConstructor
public class WebrtcAppointmentInfoController {

    private final DoctorService doctorService;
    private final AppointmentQueryService appointmentQueryService;
    private final AppointmentService appointmentService;
    private final DiagnosisService diagnosisService;
    private final MemberService memberService;
    private final AnimalService animalService;
    private final HospitalService hospitalService;

    /**
     * 비대면 진료에서 예약명단 확인
     */
    @GetMapping("/all")
    public String loadByDoctorAppointments(Model model, Principal principal) {

        LoadDoctorMyPageInfo.ResponseDto doctorDto = doctorService.findByEmail(principal.getName());

        List<AppointmentDto> appointmentDto = appointmentQueryService.findAllAppointment(doctorDto);

        model.addAttribute("appointmentDto", appointmentDto);

        return "appointments/appointmentByDoctorList";
    }


    /**
     *
     * 예약명단에서 바로 진단서 작성
     */
    @GetMapping("/all/{appointmentId}")
    public String createDiagnosisNewForm(@PathVariable("appointmentId") long appointmentId, Model model) {

        System.out.println("============ start createDiagnosisNewForm ===============");

        LoadMyPageDoctorAppointment.ResponseDto appointment = appointmentService.findById(appointmentId);

//        FindOneDiagnosis diagnosis = diagnosisService.findByAppointmentId(appointment.getId());

        model.addAttribute("appointment", appointment);
        model.addAttribute("member", appointment.getMember());
        model.addAttribute("animal", appointment.getAnimal());
        model.addAttribute("hospital", appointment.getHospital());
        model.addAttribute("doctor", appointment.getDoctor());
//        model.addAttribute("diagnosis", diagnosis);

        model.addAttribute("diagnosisForm", new FindOneDiagnosis());

        System.out.println("============ end createDiagnosisNewForm ===============");

        return "diagnosis/diagnosisForm";
    }

    @PostMapping("/all/{appointmentId}")
    public String writeNewDiagnosis(@PathVariable("appointmentId") long appointmentId,
                                    @Valid FindOneDiagnosis writtenDiagnosisForm,
                                    BindingResult result) {

        System.out.println("***************** 여기 작동 언제할거니 *****************");
        System.out.println("============ start writeNewDiagnosis ===============");

        if (result.hasErrors()) {
            return "diagnosis/diagnosisForm";
        }

        LoadMyPageDoctorAppointment.ResponseDto appointmentDto = appointmentService.findById(appointmentId);

        diagnosisService.diagnosis(appointmentDto, writtenDiagnosisForm);


        System.out.println("============ end writeNewDiagnosis ===============");


        return "redirect:/usr/mypage/doctor/member-manage/appointments/all";
    }






    // TODO 예약내역을 날짜에 따라서 분류하기
//    @GetMapping("/all2")
//    public String loadByDoctorAppointments2(@ModelAttribute("appointmentSearch") AppointmentSearch appointmentSearch, Model model, Principal principal) {
//
//        LoadDoctorMyPageInfo.ResponseDto doctorDto = doctorService.findByEmail(principal.getName());
//
////        List<AppointmentDto> appointmentDto = appointmentQueryService.findAllAppointment(doctorDto);
//
//        List<Appointment> appointmentDto = appointmentQueryService.findAppointments(appointmentSearch);
//        model.addAttribute("appointmentDto", appointmentDto);
//
//        return "appointments/appointmentByDoctorList";
////        return "redirect:/allSearch";
//    }
//
//
//    @GetMapping("/allSearch")
//    public String loadByDoctorAppointments(@ModelAttribute("appointmentSearch") AppointmentSearch appointmentSearch, Model model, Principal principal) {
//
//        List<Appointment> appointments = appointmentService.findAppointments(appointmentSearch);
//        model.addAttribute("appointments", appointments);
//
//        return "appointments/appointmentByDoctorList";
//    }


}
