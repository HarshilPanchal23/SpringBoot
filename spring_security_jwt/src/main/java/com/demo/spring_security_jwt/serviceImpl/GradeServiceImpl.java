package com.demo.spring_security_jwt.serviceImpl;

import com.demo.spring_security_jwt.dto.GradeProjection;
import com.demo.spring_security_jwt.dto.GradeRequestDto;
import com.demo.spring_security_jwt.dto.GradeResponseDto;
import com.demo.spring_security_jwt.dto.UserResponseDto;
import com.demo.spring_security_jwt.entity.GradeEntity;
import com.demo.spring_security_jwt.entity.UserEntity;
import com.demo.spring_security_jwt.enums.ExceptionEnum;
import com.demo.spring_security_jwt.exception.CustomException;
import com.demo.spring_security_jwt.repository.GradeRepository;
import com.demo.spring_security_jwt.repository.UserRepository;
import com.demo.spring_security_jwt.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    private final UserRepository userRepository;

    private final GradeRepository gradeRepository;

    private final ModelMapper modelMapper;

    public GradeEntity insertUpdateGrade(GradeRequestDto gradeRequestDto) {
        GradeEntity gradeEntity = null;
        UserEntity userEntity = userRepository.findById(gradeRequestDto.getUserId()).
                orElseThrow(() -> new CustomException(ExceptionEnum.USER_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));

        if (gradeRequestDto.getId() != null) {

            GradeEntity gradeEntity1 = gradeRepository.findById(gradeRequestDto.getId())
                    .orElseThrow(() -> new CustomException(ExceptionEnum.GRADE_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));

            gradeEntity = updateGradeEntity(gradeEntity1, gradeRequestDto, userEntity);

        } else {
            System.out.println("userEntity = " + userEntity.toString());
            gradeEntity = gradeEntity.builder()
                    .totalGrade(gradeRequestDto.getTotalGrade())
                    .userEntity(userEntity)
                    .build();
        }
        return gradeRepository.save(gradeEntity);
    }

    private GradeEntity updateGradeEntity(GradeEntity gradeEntity1, GradeRequestDto gradeRequestDto, UserEntity userEntity) {

        gradeEntity1.setTotalGrade(gradeRequestDto.getTotalGrade());
        gradeEntity1.setUserEntity(userEntity);

        return gradeEntity1;

    }

    @Override
    public GradeResponseDto insertGrade(GradeRequestDto gradeRequestDto) {
        GradeEntity gradeEntity = insertUpdateGrade(gradeRequestDto);
        System.out.println("gradeEntity = " + gradeEntity.toString());
        modelMapper.typeMap(GradeEntity.class, GradeResponseDto.class).addMappings(mapper -> {
            mapper.map(GradeEntity::getUserEntity, GradeResponseDto::setUserResponseDto);
        });
        GradeResponseDto gradeResponseDto = modelMapper.map(gradeEntity, GradeResponseDto.class);
        System.out.println("gradeResponseDto = " + gradeResponseDto.toString());
        return gradeResponseDto;
    }

    @Override
    public GradeResponseDto updateGrade(GradeRequestDto gradeRequestDto) {
        GradeEntity gradeEntity = insertUpdateGrade(gradeRequestDto);
        GradeResponseDto gradeResponseDto = modelMapper.map(gradeEntity, GradeResponseDto.class);
        return gradeResponseDto;
    }

    @Override
    public Page<GradeProjection> getGradeByUser(Long userId, String trim, Pageable pageable) {

        return gradeRepository.findByUserId(userId, pageable);

    }

    private GradeResponseDto objectTODto(GradeProjection gradeProjection) {
        GradeResponseDto gradeResponseDto = new GradeResponseDto();
        gradeResponseDto.setTotalGrade(gradeProjection.getTotalGrade());
        return gradeResponseDto;
    }
}
