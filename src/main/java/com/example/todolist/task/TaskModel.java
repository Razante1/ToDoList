package com.example.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;





@Data
@Entity(name = "task")
public class TaskModel {
    @Id
    @GeneratedValue(generator = "UUID") //GERAR ID AUTOMATICAMENTE 
    private UUID id;

    @Column(length = 50)
    private String title;
    private String description;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private String priority;

    @CreationTimestamp
    private LocalDateTime CreatedAt;

    private UUID idUser;


    public void setTitle(String title) throws Exception {
        if(title.length() > 50) {
            throw new Exception("O campo title deve conter no maximo 50 caracteres!");
        }

        this.title = title;
    }
}
