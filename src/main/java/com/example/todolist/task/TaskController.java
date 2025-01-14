package com.example.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todolist.util.Utils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/task")
public class TaskController {
    
    @Autowired
    private ITaskRepository taskRepository;



    @SuppressWarnings("rawtypes")
    @PostMapping
    public ResponseEntity createTastk (@RequestBody TaskModel taskModel , HttpServletRequest request) {

        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID)idUser);
        


        var currentDate = LocalDateTime.now();

        if(currentDate.isAfter(taskModel.getStartsAt()) || currentDate.isAfter(taskModel.getEndsAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio da taks tem que ser depois da data atual");
        } else {
            var task = this.taskRepository.save(taskModel);
            return ResponseEntity.status(HttpStatus.OK).body(task);
        }    
    }

    @GetMapping //GET de tudo que engloba o /task 
    public List<TaskModel> list (HttpServletRequest request) {
        var idUser = request.getAttribute("idUser"); //requisição para pegar valores referentes ao 
        var teste = this.taskRepository.findByIdUser((UUID)idUser);
        System.out.println(teste);
        return teste;
    }
    @SuppressWarnings("rawtypes")
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request , @PathVariable UUID id) {
        
        var idUser = request.getAttribute("idUser");

        

        var task = this.taskRepository.findById(id).orElse(null);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não existe");
        }

        Utils.copyNonNullProperties(taskModel, task);

        if(!taskModel.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task não atrelada ao ususario");
        }

        var taskUpdated = this.taskRepository.save(task);
        return ResponseEntity.ok().body(this.taskRepository.save(taskUpdated));
    }
}
