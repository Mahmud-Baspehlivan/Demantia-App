package com.demantia.demantia.service;

import com.demantia.demantia.model.TestResponse;
import com.demantia.demantia.model.Question;
import com.demantia.demantia.repository.TestResponseRepository;
import com.demantia.demantia.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class TestResponseService {

    @Autowired
    private TestResponseRepository testResponseRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public TestResponse getResponseById(String id) throws ExecutionException, InterruptedException {
        return testResponseRepository.getResponseById(id);
    }

    public List<TestResponse> getAllResponses() throws ExecutionException, InterruptedException {
        return testResponseRepository.getAllResponses();
    }

    public List<TestResponse> getResponsesBySessionId(String sessionId)
            throws ExecutionException, InterruptedException {
        return testResponseRepository.getResponsesBySessionId(sessionId);
    }

    public List<TestResponse> getResponsesByPatientId(String patientId)
            throws ExecutionException, InterruptedException {
        return testResponseRepository.getResponsesByPatientId(patientId);
    }

    public List<TestResponse> getResponsesByQuestionId(String questionId)
            throws ExecutionException, InterruptedException {
        return testResponseRepository.getResponsesByQuestionId(questionId);
    }

    public TestResponse getResponseBySessionAndQuestionId(String sessionId, String questionId)
            throws ExecutionException, InterruptedException {
        return testResponseRepository.getResponseBySessionAndQuestionId(sessionId, questionId);
    }

    public String createResponse(TestResponse response) throws ExecutionException, InterruptedException {
        return testResponseRepository.createResponse(response);
    }

    public String createResponseWithScoring(TestResponse response, String answer)
            throws ExecutionException, InterruptedException {
        // Sorunun doğru cevabıyla karşılaştırmak için
        Question question = questionRepository.getQuestionById(response.getQuestion_id());
        if (question == null) {
            throw new IllegalArgumentException("Geçersiz soru ID'si: " + response.getQuestion_id());
        }

        // Response değerini ayarla
        Map<String, Object> responseValues = new HashMap<>();
        responseValues.put("answer", answer);
        response.setResponse(responseValues);

        // Cevaba göre puan atama
        calculateScore(response, question, answer);

        return testResponseRepository.createResponse(response);
    }

    public String updateResponse(TestResponse response) throws ExecutionException, InterruptedException {
        return testResponseRepository.updateResponse(response);
    }

    public String deleteResponse(String id) throws ExecutionException, InterruptedException {
        return testResponseRepository.deleteResponse(id);
    }

    // İş mantığı için yardımcı metodlar

    private void calculateScore(TestResponse response, Question question, String answer) {
        // Çeşitli soru tiplerine göre puanlama stratejisi
        if (question.getQuestion_type().equals("multiple_choice")) {
            Map<String, String> optionsMap = question.getOptionsAsMap();

            // Multiple choice soruların puanlaması - örnek bir puanlama mantığı
            if (optionsMap != null && optionsMap.containsKey("1") && optionsMap.get("1").equals(answer)) {
                response.setScore_weight(1.0); // Doğru cevap için tam puan
            } else {
                response.setScore_weight(0.0); // Yanlış cevap için puan yok
            }
        } else if (question.getQuestion_type().equals("multiple_select")) {
            // Multiple select soruların puanlaması
            // Burada daha karmaşık bir puanlama mantığı olabilir
            response.setScore_weight(0.5);
        } else {
            // Varsayılan puanlama
            response.setScore_weight(1.0);
        }
    }

    public Map<String, Double> calculateCategoryScores(String sessionId)
            throws ExecutionException, InterruptedException {
        List<TestResponse> responses = getResponsesBySessionId(sessionId);
        Map<String, Double> categoryScores = new HashMap<>();

        for (TestResponse response : responses) {
            Question question = questionRepository.getQuestionById(response.getQuestion_id());
            if (question != null) {
                String category = question.getCategory();
                Double score = response.getScoreWeightAsDouble();

                if (score != null) {
                    categoryScores.put(category, categoryScores.getOrDefault(category, 0.0) + score);
                }
            }
        }

        return categoryScores;
    }
}
