package com.demantia.demantia.service;

import com.demantia.demantia.model.TestResult;
import com.demantia.demantia.repository.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class TestResultService {

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private TestResponseService testResponseService;

    public TestResult getTestResultById(String id) throws ExecutionException, InterruptedException {
        return testResultRepository.getTestResultById(id);
    }

    public List<TestResult> getAllTestResults() throws ExecutionException, InterruptedException {
        return testResultRepository.getAllTestResults();
    }

    public List<TestResult> getTestResultsByPatientId(String patientId)
            throws ExecutionException, InterruptedException {
        return testResultRepository.getTestResultsByPatientId(patientId);
    }

    public TestResult getTestResultBySessionId(String sessionId) throws ExecutionException, InterruptedException {
        return testResultRepository.getTestResultBySessionId(sessionId);
    }

    public List<TestResult> getTestResultsByRiskLevel(String riskLevel)
            throws ExecutionException, InterruptedException {
        return testResultRepository.getTestResultsByRiskLevel(riskLevel);
    }

    public String createTestResult(TestResult testResult) throws ExecutionException, InterruptedException {
        return testResultRepository.createTestResult(testResult);
    }

    public String generateTestResult(String sessionId, String patientId)
            throws ExecutionException, InterruptedException {
        // Test yanıtlarından puan hesaplama
        Map<String, Double> categoryScores = testResponseService.calculateCategoryScores(sessionId);

        // Toplam puanı hesapla
        int overallScore = calculateOverallScore(categoryScores);

        // Risk seviyesini belirle
        String riskLevel = determineRiskLevel(overallScore);

        // Tavsiye oluştur
        String recommendation = generateRecommendation(riskLevel, overallScore);

        // Test sonucunu oluştur
        TestResult testResult = new TestResult();
        testResult.setTest_session_id(sessionId);
        testResult.setPatient_id(patientId);
        testResult.setOverall_score(overallScore);
        testResult.setRisk_level(riskLevel);
        testResult.setRecommendation(recommendation);

        return testResultRepository.createTestResult(testResult);
    }

    public String updateTestResult(TestResult testResult) throws ExecutionException, InterruptedException {
        return testResultRepository.updateTestResult(testResult);
    }

    public String deleteTestResult(String id) throws ExecutionException, InterruptedException {
        return testResultRepository.deleteTestResult(id);
    }

    // İş mantığı için yardımcı metodlar

    private int calculateOverallScore(Map<String, Double> categoryScores) {
        double totalScore = categoryScores.values().stream().mapToDouble(Double::doubleValue).sum();
        double maxPossibleScore = categoryScores.size() * 10.0; // Her kategori için maksimum 10 puan varsayalım

        // 0-100 arası bir puana normalize et
        return (int) Math.round((totalScore / maxPossibleScore) * 100);
    }

    private String determineRiskLevel(int overallScore) {
        if (overallScore >= 80) {
            return "low";
        } else if (overallScore >= 60) {
            return "medium";
        } else {
            return "high";
        }
    }

    private String generateRecommendation(String riskLevel, int overallScore) {
        switch (riskLevel) {
            case "low":
                return "Düşük risk seviyesi. Yıllık kontrollerle takip önerilir.";
            case "medium":
                return "Orta risk seviyesi. 6 aylık periyotlarla kontrole gelmeniz ve bilişsel aktiviteleri artırmanız önerilir.";
            case "high":
                return "Yüksek risk seviyesi. En kısa sürede bir nöroloji uzmanına başvurmanız önerilir.";
            default:
                return "Değerlendirme sonuçlarınız için bir uzman ile görüşmeniz önerilir.";
        }
    }
}
