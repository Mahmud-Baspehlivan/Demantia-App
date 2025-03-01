package com.demantia.demantia.service;

import com.demantia.demantia.model.TestSession;
import com.demantia.demantia.repository.TestSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class TestSessionService {

    @Autowired
    private TestSessionRepository testSessionRepository;

    public TestSession getSessionById(String id) throws ExecutionException, InterruptedException {
        return testSessionRepository.getSessionById(id);
    }

    public List<TestSession> getAllSessions() throws ExecutionException, InterruptedException {
        return testSessionRepository.getAllSessions();
    }

    public List<TestSession> getSessionsByPatientId(String patientId) throws ExecutionException, InterruptedException {
        return testSessionRepository.getSessionsByPatientId(patientId);
    }

    public List<TestSession> getSessionsByDoctorId(String doctorId) throws ExecutionException, InterruptedException {
        return testSessionRepository.getSessionsByDoctorId(doctorId);
    }

    public List<TestSession> getSessionsByStatus(String status) throws ExecutionException, InterruptedException {
        return testSessionRepository.getSessionsByStatus(status);
    }

    public List<TestSession> getCompletedSessionsByPatientId(String patientId)
            throws ExecutionException, InterruptedException {
        return testSessionRepository.getCompletedSessionsByPatientId(patientId);
    }

    public TestSession getLatestSessionByPatientId(String patientId) throws ExecutionException, InterruptedException {
        return testSessionRepository.getLatestSessionByPatientId(patientId);
    }

    public String createSession(TestSession session) throws ExecutionException, InterruptedException {
        // Başlangıç zamanını ayarla
        Map<String, Object> startTime = new HashMap<>();
        startTime.put("seconds", System.currentTimeMillis() / 1000);
        startTime.put("nanos", 0);
        session.setStart_time(startTime);

        // Oturum durumunu "in_progress" olarak ayarla
        session.setStatus("in_progress");

        return testSessionRepository.createSession(session);
    }

    public String createSessionWithQuestions(String patientId, String doctorId, List<String> questionIds)
            throws ExecutionException, InterruptedException {
        // Yeni bir test oturumu oluştur
        TestSession session = new TestSession();
        session.setPatient_id(patientId);
        session.setDoctor_id(doctorId);
        session.setStatus("in_progress");

        // Başlangıç zamanını ayarla
        Map<String, Object> startTime = new HashMap<>();
        startTime.put("seconds", System.currentTimeMillis() / 1000);
        startTime.put("nanos", 0);
        session.setStart_time(startTime);

        // Oturum oluştur ve ID'sini al
        String sessionId = testSessionRepository.createSession(session);

        return sessionId;
    }

    public String updateSession(TestSession session) throws ExecutionException, InterruptedException {
        return testSessionRepository.updateSession(session);
    }

    public String completeSession(String sessionId) throws ExecutionException, InterruptedException {
        TestSession session = getSessionById(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Geçersiz oturum ID'si: " + sessionId);
        }

        // Bitiş zamanını ayarla
        Map<String, Object> endTime = new HashMap<>();
        endTime.put("seconds", System.currentTimeMillis() / 1000);
        endTime.put("nanos", 0);
        session.setEnd_time(endTime);

        // Oturum durumunu "completed" olarak ayarla
        session.setStatus("completed");

        return testSessionRepository.updateSession(session);
    }

    public String deleteSession(String id) throws ExecutionException, InterruptedException {
        return testSessionRepository.deleteSession(id);
    }

    // İş mantığı için yardımcı metodlar

    public TestSession getActiveSessionForPatient(String patientId) throws ExecutionException, InterruptedException {
        List<TestSession> sessions = getSessionsByPatientId(patientId);
        return sessions.stream()
                .filter(s -> "in_progress".equals(s.getStatus()))
                .findFirst()
                .orElse(null);
    }

    public long calculateSessionDurationSeconds(TestSession session) {
        if (session.getStart_time() == null || session.getEnd_time() == null) {
            return -1; // Süresi hesaplanamaz
        }

        Long startTimeSeconds = session.getStartTimeAsLong();
        Long endTimeSeconds = session.getEndTimeAsLong();

        if (startTimeSeconds == null || endTimeSeconds == null) {
            return -1;
        }

        return endTimeSeconds - startTimeSeconds;
    }
}
