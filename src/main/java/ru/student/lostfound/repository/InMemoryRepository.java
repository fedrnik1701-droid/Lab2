package ru.student.lostfound.repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryRepository<T> {
    private final Map<Long, T> data = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public long nextId() { return sequence.incrementAndGet(); }
    public List<T> findAll() { return new ArrayList<>(data.values()); }
    public Optional<T> findById(long id) { return Optional.ofNullable(data.get(id)); }
    public void save(long id, T value) { data.put(id, value); }
    public boolean delete(long id) { return data.remove(id) != null; }
    public void deleteWhere(java.util.function.Predicate<T> predicate) {
        data.entrySet().removeIf(e -> predicate.test(e.getValue()));
    }
}
