package com.example.springSecurity.serviceImpl;

import com.example.springSecurity.dto.GradeProjection;
import com.example.springSecurity.dto.GradeRequestDto;
import com.example.springSecurity.dto.GradeResponseDto;
import com.example.springSecurity.dto.UserResponseDto;
import com.example.springSecurity.entity.GradeEntity;
import com.example.springSecurity.entity.UserEntity;
import com.example.springSecurity.enums.ExceptionEnum;
import com.example.springSecurity.exception.CustomException;
import com.example.springSecurity.repository.GradeRepository;
import com.example.springSecurity.repository.UserRepository;
import com.example.springSecurity.service.GradeService;
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
