package ru.student.lostfound.service;

import org.springframework.stereotype.Service;
import ru.student.lostfound.model.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class HandoverService {
    private final Repositories r;
    public HandoverService(Repositories r){this.r=r;}
    public List<HandoverRecord> all(){return r.handovers.findAll();}
    public HandoverRecord get(long id){return r.handovers.findById(id).orElseThrow(()->new NotFoundException("Акт передачи "+id+" не найден"));}
    public HandoverRecord create(Long itemClaimId,Boolean confirmed){
        if(itemClaimId==null||confirmed==null)throw new BadRequestException("itemClaimId и confirmed обязательны");
        if(r.itemClaims.findById(itemClaimId).isEmpty())throw new BadRequestException("Претензия не найдена");
        long id=r.handovers.nextId();
        // Дата всегда определяется сервером, поле handoverDate из запроса не принимается.
        HandoverRecord x=new HandoverRecord(id,itemClaimId,LocalDateTime.now(),confirmed);r.handovers.save(id,x);return x;
    }
    public HandoverRecord update(long id,Long itemClaimId,Boolean confirmed){
        HandoverRecord o=get(id);Long cid=itemClaimId==null?o.itemClaimId():itemClaimId;
        if(r.itemClaims.findById(cid).isEmpty())throw new BadRequestException("Претензия не найдена");
        HandoverRecord x=new HandoverRecord(id,cid,o.handoverDate(),confirmed==null?o.confirmed():confirmed);r.handovers.save(id,x);return x;
    }
    public void delete(long id){get(id);r.handovers.delete(id);}
}
