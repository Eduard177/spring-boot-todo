package com.pichardo.SpringTodoApp.mapper;

import com.pichardo.SpringTodoApp.dto.NewTaskDto;
import com.pichardo.SpringTodoApp.dto.NewUserDto;
import com.pichardo.SpringTodoApp.models.Task;
import com.pichardo.SpringTodoApp.models.User;
import org.modelmapper.ModelMapper;


public class EntityMapper {

    public User newUserDtoToUser(NewUserDto newUserDto ){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(newUserDto, User.class);
    }

    public Task newTaskDtooToTask(NewTaskDto newTaskDto){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(newTaskDto, Task.class);
    }
}
