package ru.student.lostfound.service;

import org.springframework.stereotype.Component;
import ru.student.lostfound.model.*;
import ru.student.lostfound.repository.InMemoryRepository;

@Component
public class Repositories {
    public final InMemoryRepository<User> users = new InMemoryRepository<>();
    public final InMemoryRepository<FoundItem> foundItems = new InMemoryRepository<>();
    public final InMemoryRepository<LossReport> lossReports = new InMemoryRepository<>();
    public final InMemoryRepository<ItemClaim> itemClaims = new InMemoryRepository<>();
    public final InMemoryRepository<HandoverRecord> handovers = new InMemoryRepository<>();
}
