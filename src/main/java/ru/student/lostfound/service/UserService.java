package ru.student.lostfound.service;

import org.springframework.stereotype.Service;
import ru.student.lostfound.model.User;
import java.util.*;

@Service
public class UserService {
    private final Repositories r;
    public UserService(Repositories r) { this.r = r; }
    public List<User> all() { return r.users.findAll(); }
    public User get(long id) { return r.users.findById(id).orElseThrow(() -> new NotFoundException("Пользователь " + id + " не найден")); }
    public User create(String name, String email) {
        require(name, "name"); require(email, "email");
        long id = r.users.nextId(); User u = new User(id, name, email); r.users.save(id, u); return u;
    }
    public User update(long id, String name, String email) {
        User old = get(id); User u = new User(id, name == null ? old.name() : name, email == null ? old.email() : email); r.users.save(id, u); return u;
    }
    public void delete(long id) {
        get(id);
        // Referential cleanup: deleting a user removes their loss reports and dependent claims/handover records.
        Set<Long> reportIds = new HashSet<>();
        r.lossReports.findAll().stream().filter(x -> x.userId().equals(id)).forEach(x -> reportIds.add(x.id()));
        Set<Long> claimIds = new HashSet<>();
        r.itemClaims.findAll().stream().filter(x -> reportIds.contains(x.lossReportId())).forEach(x -> claimIds.add(x.id()));
        r.handovers.deleteWhere(x -> claimIds.contains(x.itemClaimId()));
        r.itemClaims.deleteWhere(x -> reportIds.contains(x.lossReportId()));
        r.lossReports.deleteWhere(x -> x.userId().equals(id));
        r.users.delete(id);
    }
    private void require(String v, String field) { if (v == null || v.isBlank()) throw new BadRequestException(field + " обязателен"); }
}
