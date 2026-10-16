package ru.student.lostfound.service;

import org.springframework.stereotype.Service;
import ru.student.lostfound.model.*;
import java.util.*;

@Service
public class ItemClaimService {
    private final Repositories r;
    public ItemClaimService(Repositories r) { this.r = r; }
    public List<ItemClaim> all(){return r.itemClaims.findAll();}
    public ItemClaim get(long id){return r.itemClaims.findById(id).orElseThrow(()->new NotFoundException("Претензия "+id+" не найдена"));}
    public ItemClaim create(Long lossReportId,Long foundItemId,String evidence,String status){
        check(lossReportId,foundItemId,evidence,status);
        if(r.lossReports.findById(lossReportId).isEmpty()||r.foundItems.findById(foundItemId).isEmpty())throw new BadRequestException("Заявление или найденный предмет не существуют");
        long id=r.itemClaims.nextId();ItemClaim x=new ItemClaim(id,lossReportId,foundItemId,evidence,status);r.itemClaims.save(id,x);return x;
    }
    public ItemClaim update(long id,Long lossReportId,Long foundItemId,String evidence,String status){
        ItemClaim o=get(id);Long lid=lossReportId==null?o.lossReportId():lossReportId;Long fid=foundItemId==null?o.foundItemId():foundItemId;
        if(r.lossReports.findById(lid).isEmpty()||r.foundItems.findById(fid).isEmpty())throw new BadRequestException("Заявление или найденный предмет не существуют");
        ItemClaim x=new ItemClaim(id,lid,fid,evidence==null?o.evidence():evidence,status==null?o.status():status);r.itemClaims.save(id,x);return x;
    }
    public void delete(long id){get(id);r.handovers.deleteWhere(x->x.itemClaimId()==id);r.itemClaims.delete(id);}
    private void check(Long l,Long f,String...s){if(l==null||f==null)throw new BadRequestException("lossReportId и foundItemId обязательны");for(String x:s)if(x==null||x.isBlank())throw new BadRequestException("Все поля претензии обязательны");}
}
