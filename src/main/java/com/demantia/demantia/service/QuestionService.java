package com.demantia.demantia.service;

import com.demantia.demantia.model.Question;
import com.demantia.demantia.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public Question getQuestionById(String id) throws ExecutionException, InterruptedException {
        return questionRepository.getQuestionById(id);
    }

    public List<Question> getAllQuestions() throws ExecutionException, InterruptedException {
        return questionRepository.getAllQuestions();
    }

    public List<Question> getQuestionsByCategory(String category) throws ExecutionException, InterruptedException {
        return questionRepository.getQuestionsByCategory(category);
    }

    public List<Question> getQuestionsByRelatedGroup(String relatedGroup)
            throws ExecutionException, InterruptedException {
        return questionRepository.getQuestionsByRelatedGroup(relatedGroup);
    }

    public List<Question> getQuestionsByDifficultyLevel(String difficultyLevel)
            throws ExecutionException, InterruptedException {
        return questionRepository.getQuestionsByDifficultyLevel(difficultyLevel);
    }

    public List<Question> getQuestionsByIds(List<String> ids) throws ExecutionException, InterruptedException {
        return questionRepository.getQuestionsByIds(ids);
    }

    public String createQuestion(Question question) throws ExecutionException, InterruptedException {
        return questionRepository.createQuestion(question);
    }

    public String updateQuestion(Question question) throws ExecutionException, InterruptedException {
        return questionRepository.updateQuestion(question);
    }

    public String deleteQuestion(String id) throws ExecutionException, InterruptedException {
        return questionRepository.deleteQuestion(id);
    }

    // İş mantığı için yardımcı metodlar

    public List<Question> getQuestionsForPatientAssessment(String category, String difficultyLevel)
            throws ExecutionException, InterruptedException {
        // Belirli kategori ve zorluk seviyesine göre soru filtreleme
        List<Question> questions = getQuestionsByCategory(category);

        return questions.stream()
                .filter(q -> q.getDifficulty_level().equals(difficultyLevel))
                .toList();
    }

    public Question getFollowUpQuestion(String questionId) throws ExecutionException, InterruptedException {
        Question question = getQuestionById(questionId);
        if (question != null && question.getFollowup_question_id() != null) {
            return getQuestionById(question.getFollowup_question_id());
        }
        return null;
    }
}
