package com.common.core.mapper;

import com.common.core.dto.UserCreateDTO;
import com.common.core.dto.UserQueryDTO;
import com.common.core.dto.UserUpdateDTO;
import com.common.core.entity.SysUser;
import com.common.core.vo.UserDetailVO;
import com.common.core.vo.UserVO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-01T15:54:40+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class UserConvertMapperImpl implements UserConvertMapper {

    @Override
    public UserVO toUserVO(SysUser sysUser) {
        if ( sysUser == null ) {
            return null;
        }

        UserVO userVO = new UserVO();

        userVO.setId( sysUser.getId() );
        userVO.setUsername( sysUser.getUsername() );
        userVO.setNickname( sysUser.getNickname() );
        if ( sysUser.hasEmail() ) {
            userVO.setEmail( sysUser.getEmail() );
        }
        if ( sysUser.hasPhone() ) {
            userVO.setPhone( sysUser.getPhone() );
        }
        if ( sysUser.hasAvatar() ) {
            userVO.setAvatar( sysUser.getAvatar() );
        }
        userVO.setGender( sysUser.getGender() );
        userVO.setBirthday( sysUser.getBirthday() );
        userVO.setStatus( sysUser.getStatus() );
        userVO.setLoginIp( sysUser.getLoginIp() );
        userVO.setLoginTime( sysUser.getLoginTime() );
        userVO.setRemark( sysUser.getRemark() );
        userVO.setCreateTime( sysUser.getCreateTime() );
        userVO.setUpdateTime( sysUser.getUpdateTime() );
        userVO.setVersion( sysUser.getVersion() );
        userVO.setDisplayName( sysUser.getDisplayName() );

        return userVO;
    }

    @Override
    public List<UserVO> toUserVOList(List<SysUser> sysUserList) {
        if ( sysUserList == null ) {
            return null;
        }

        List<UserVO> list = new ArrayList<UserVO>( sysUserList.size() );
        for ( SysUser sysUser : sysUserList ) {
            list.add( toUserVO( sysUser ) );
        }

        return list;
    }

    @Override
    public UserDetailVO toUserDetailVO(SysUser sysUser) {
        if ( sysUser == null ) {
            return null;
        }

        UserDetailVO userDetailVO = new UserDetailVO();

        userDetailVO.setId( sysUser.getId() );
        userDetailVO.setUsername( sysUser.getUsername() );
        userDetailVO.setNickname( sysUser.getNickname() );
        if ( sysUser.hasEmail() ) {
            userDetailVO.setEmail( sysUser.getEmail() );
        }
        if ( sysUser.hasPhone() ) {
            userDetailVO.setPhone( sysUser.getPhone() );
        }
        if ( sysUser.hasAvatar() ) {
            userDetailVO.setAvatar( sysUser.getAvatar() );
        }
        userDetailVO.setGender( sysUser.getGender() );
        userDetailVO.setBirthday( sysUser.getBirthday() );
        userDetailVO.setStatus( sysUser.getStatus() );
        userDetailVO.setLoginIp( sysUser.getLoginIp() );
        userDetailVO.setLoginTime( sysUser.getLoginTime() );
        userDetailVO.setRemark( sysUser.getRemark() );
        userDetailVO.setCreateTime( sysUser.getCreateTime() );
        userDetailVO.setUpdateTime( sysUser.getUpdateTime() );
        userDetailVO.setVersion( sysUser.getVersion() );

        userDetailVO.setGenderText( getGenderName(sysUser.getGender()) );
        userDetailVO.setStatusText( getStatusName(sysUser.getStatus()) );
        userDetailVO.setDisplayName( getDisplayName(sysUser) );
        userDetailVO.setAge( calculateAge(sysUser.getBirthday()) );

        return userDetailVO;
    }

    @Override
    public SysUser createDTOToEntity(UserCreateDTO createDTO) {
        if ( createDTO == null ) {
            return null;
        }

        SysUser sysUser = new SysUser();

        sysUser.setRemark( createDTO.getRemark() );
        sysUser.setUsername( createDTO.getUsername() );
        sysUser.setPassword( createDTO.getPassword() );
        sysUser.setNickname( createDTO.getNickname() );
        sysUser.setEmail( createDTO.getEmail() );
        sysUser.setPhone( createDTO.getPhone() );
        sysUser.setGender( createDTO.getGender() );
        sysUser.setBirthday( createDTO.getBirthday() );
        sysUser.setStatus( createDTO.getStatus() );

        return sysUser;
    }

    @Override
    public SysUser updateDTOToEntity(UserUpdateDTO updateDTO) {
        if ( updateDTO == null ) {
            return null;
        }

        SysUser sysUser = new SysUser();

        sysUser.setRemark( updateDTO.getRemark() );
        sysUser.setUsername( updateDTO.getUsername() );
        sysUser.setNickname( updateDTO.getNickname() );
        sysUser.setEmail( updateDTO.getEmail() );
        sysUser.setPhone( updateDTO.getPhone() );
        sysUser.setAvatar( updateDTO.getAvatar() );
        sysUser.setGender( updateDTO.getGender() );
        sysUser.setBirthday( updateDTO.getBirthday() );
        sysUser.setStatus( updateDTO.getStatus() );

        return sysUser;
    }

    @Override
    public void updateEntityFromDTO(UserUpdateDTO updateDTO, SysUser sysUser) {
        if ( updateDTO == null ) {
            return;
        }

        if ( updateDTO.getRemark() != null ) {
            sysUser.setRemark( updateDTO.getRemark() );
        }
        if ( updateDTO.getUsername() != null ) {
            sysUser.setUsername( updateDTO.getUsername() );
        }
        if ( updateDTO.getNickname() != null ) {
            sysUser.setNickname( updateDTO.getNickname() );
        }
        if ( updateDTO.getEmail() != null ) {
            sysUser.setEmail( updateDTO.getEmail() );
        }
        if ( updateDTO.getPhone() != null ) {
            sysUser.setPhone( updateDTO.getPhone() );
        }
        if ( updateDTO.getAvatar() != null ) {
            sysUser.setAvatar( updateDTO.getAvatar() );
        }
        if ( updateDTO.getGender() != null ) {
            sysUser.setGender( updateDTO.getGender() );
        }
        if ( updateDTO.getBirthday() != null ) {
            sysUser.setBirthday( updateDTO.getBirthday() );
        }
        if ( updateDTO.getStatus() != null ) {
            sysUser.setStatus( updateDTO.getStatus() );
        }
    }

    @Override
    public UserQueryDTO toUserQueryDTO(SysUser sysUser) {
        if ( sysUser == null ) {
            return null;
        }

        UserQueryDTO userQueryDTO = new UserQueryDTO();

        userQueryDTO.setUsername( sysUser.getUsername() );
        userQueryDTO.setNickname( sysUser.getNickname() );
        if ( sysUser.hasEmail() ) {
            userQueryDTO.setEmail( sysUser.getEmail() );
        }
        if ( sysUser.hasPhone() ) {
            userQueryDTO.setPhone( sysUser.getPhone() );
        }
        userQueryDTO.setGender( sysUser.getGender() );
        userQueryDTO.setStatus( sysUser.getStatus() );
        userQueryDTO.setCreateBy( sysUser.getCreateBy() );
        userQueryDTO.setLoginIp( sysUser.getLoginIp() );

        return userQueryDTO;
    }

    @Override
    public UserUpdateDTO toUserUpdateDTO(SysUser sysUser) {
        if ( sysUser == null ) {
            return null;
        }

        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();

        userUpdateDTO.setId( sysUser.getId() );
        userUpdateDTO.setUsername( sysUser.getUsername() );
        userUpdateDTO.setNickname( sysUser.getNickname() );
        if ( sysUser.hasEmail() ) {
            userUpdateDTO.setEmail( sysUser.getEmail() );
        }
        if ( sysUser.hasPhone() ) {
            userUpdateDTO.setPhone( sysUser.getPhone() );
        }
        if ( sysUser.hasAvatar() ) {
            userUpdateDTO.setAvatar( sysUser.getAvatar() );
        }
        userUpdateDTO.setGender( sysUser.getGender() );
        userUpdateDTO.setBirthday( sysUser.getBirthday() );
        userUpdateDTO.setStatus( sysUser.getStatus() );
        userUpdateDTO.setRemark( sysUser.getRemark() );
        userUpdateDTO.setVersion( sysUser.getVersion() );

        return userUpdateDTO;
    }

    @Override
    public SysUser copyEntityBasicInfo(SysUser source) {
        if ( source == null ) {
            return null;
        }

        SysUser sysUser = new SysUser();

        sysUser.setId( source.getId() );
        sysUser.setCreateTime( source.getCreateTime() );
        sysUser.setUpdateTime( source.getUpdateTime() );
        sysUser.setCreateBy( source.getCreateBy() );
        sysUser.setUpdateBy( source.getUpdateBy() );
        sysUser.setVersion( source.getVersion() );
        sysUser.setRemark( source.getRemark() );
        sysUser.setGenderEnum( source.getGenderEnum() );
        sysUser.setStatusEnum( source.getStatusEnum() );
        sysUser.setUsername( source.getUsername() );
        sysUser.setNickname( source.getNickname() );
        if ( source.hasEmail() ) {
            sysUser.setEmail( source.getEmail() );
        }
        if ( source.hasPhone() ) {
            sysUser.setPhone( source.getPhone() );
        }
        if ( source.hasAvatar() ) {
            sysUser.setAvatar( source.getAvatar() );
        }
        sysUser.setGender( source.getGender() );
        sysUser.setBirthday( source.getBirthday() );
        sysUser.setStatus( source.getStatus() );
        sysUser.setLoginIp( source.getLoginIp() );
        sysUser.setLoginTime( source.getLoginTime() );

        return sysUser;
    }

    @Override
    public List<UserDetailVO> toUserDetailVOList(List<SysUser> sysUserList) {
        if ( sysUserList == null ) {
            return null;
        }

        List<UserDetailVO> list = new ArrayList<UserDetailVO>( sysUserList.size() );
        for ( SysUser sysUser : sysUserList ) {
            list.add( toUserDetailVO( sysUser ) );
        }

        return list;
    }
}
