package com.demantia.demantia.repository;

import com.demantia.demantia.model.DemansGroup;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface DemansGroupRepository {
    DemansGroup getGroupById(String id) throws ExecutionException, InterruptedException;

    List<DemansGroup> getAllGroups() throws ExecutionException, InterruptedException;

    DemansGroup getGroupByAgeRange(String ageRange) throws ExecutionException, InterruptedException;

    String createGroup(DemansGroup group) throws ExecutionException, InterruptedException;

    String updateGroup(DemansGroup group) throws ExecutionException, InterruptedException;

    String deleteGroup(String id) throws ExecutionException, InterruptedException;
}
