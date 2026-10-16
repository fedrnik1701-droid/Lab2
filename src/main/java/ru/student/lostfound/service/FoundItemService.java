package ru.student.lostfound.service;

import org.springframework.stereotype.Service;
import ru.student.lostfound.model.*;
import java.util.*;

@Service
public class FoundItemService {
    private final Repositories r;
    public FoundItemService(Repositories r) { this.r = r; }
    public List<FoundItem> all() { return r.foundItems.findAll(); }
    public FoundItem get(long id) { return r.foundItems.findById(id).orElseThrow(() -> new NotFoundException("Найденный предмет " + id + " не найден")); }
    public FoundItem create(String description, String category, String foundLocation, String status) {
        check(description, category, foundLocation, status);
        long id = r.foundItems.nextId(); FoundItem x = new FoundItem(id, description, category, foundLocation, status); r.foundItems.save(id, x); return x;
    }
    public FoundItem update(long id, String description, String category, String foundLocation, String status) {
        FoundItem o = get(id); FoundItem x = new FoundItem(id, val(description,o.description()), val(category,o.category()), val(foundLocation,o.foundLocation()), val(status,o.status())); r.foundItems.save(id,x); return x;
    }
    public void delete(long id) {
        get(id);
        Set<Long> claimIds = new HashSet<>();
        r.itemClaims.findAll().stream().filter(x -> x.foundItemId().equals(id)).forEach(x -> claimIds.add(x.id()));
        r.handovers.deleteWhere(x -> claimIds.contains(x.itemClaimId()));
        r.itemClaims.deleteWhere(x -> x.foundItemId().equals(id));
        r.foundItems.delete(id);
    }
    private String val(String a,String b){return a==null?b:a;}
    private void check(String... values){ for(String v:values) if(v==null||v.isBlank()) throw new BadRequestException("Все поля предмета обязательны"); }
}
