package ru.student.lostfound.service;

import org.springframework.stereotype.Service;
import ru.student.lostfound.model.*;
import java.util.*;

@Service
public class LossReportService {
    private final Repositories r;
    public LossReportService(Repositories r) { this.r = r; }
    public List<LossReport> all() { return r.lossReports.findAll(); }
    public LossReport get(long id) { return r.lossReports.findById(id).orElseThrow(() -> new NotFoundException("Заявление " + id + " не найдено")); }
    public LossReport create(Long userId, String description, String category, String status) {
        check(userId,description,category,status); if(r.users.findById(userId).isEmpty()) throw new BadRequestException("Пользователь не найден");
        long id=r.lossReports.nextId(); LossReport x=new LossReport(id,userId,description,category,status); r.lossReports.save(id,x); return x;
    }
    public LossReport update(long id, Long userId, String description, String category, String status) {
        LossReport o=get(id); Long uid=userId==null?o.userId():userId;
        if(r.users.findById(uid).isEmpty()) throw new BadRequestException("Пользователь не найден");
        LossReport x=new LossReport(id,uid,description==null?o.description():description,category==null?o.category():category,status==null?o.status():status);r.lossReports.save(id,x);return x;
    }
    public void delete(long id) {
        get(id); Set<Long> ids=new HashSet<>();
        r.itemClaims.findAll().stream().filter(x->x.lossReportId().equals(id)).forEach(x->ids.add(x.id()));
        r.handovers.deleteWhere(x->ids.contains(x.itemClaimId())); r.itemClaims.deleteWhere(x->x.lossReportId().equals(id)); r.lossReports.delete(id);
    }
    private void check(Long u,String...s){if(u==null)throw new BadRequestException("userId обязателен");for(String x:s)if(x==null||x.isBlank())throw new BadRequestException("Все поля заявления обязательны");}
}
