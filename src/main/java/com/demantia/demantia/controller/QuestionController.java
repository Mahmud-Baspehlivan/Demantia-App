package com.demantia.demantia.controller;

import com.demantia.demantia.model.Question;
import com.demantia.demantia.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            List<Question> questions = questionService.getAllQuestions();
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable String id) {
        try {
            Question question = questionService.getQuestionById(id);
            if (question != null) {
                return new ResponseEntity<>(question, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable String category) {
        try {
            List<Question> questions = questionService.getQuestionsByCategory(category);
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/related-group/{relatedGroup}")
    public ResponseEntity<List<Question>> getQuestionsByRelatedGroup(@PathVariable String relatedGroup) {
        try {
            List<Question> questions = questionService.getQuestionsByRelatedGroup(relatedGroup);
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/difficulty/{difficultyLevel}")
    public ResponseEntity<List<Question>> getQuestionsByDifficultyLevel(@PathVariable String difficultyLevel) {
        try {
            List<Question> questions = questionService.getQuestionsByDifficultyLevel(difficultyLevel);
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<String> createQuestion(@RequestBody Question question) {
        try {
            String id = questionService.createQuestion(question);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateQuestion(@PathVariable String id, @RequestBody Question question) {
        try {
            question.setId(id);
            String result = questionService.updateQuestion(question);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable String id) {
        try {
            String result = questionService.deleteQuestion(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
