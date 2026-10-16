package ru.student.lostfound.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.student.lostfound.model.ItemClaim;
import ru.student.lostfound.service.ItemClaimService;
import java.util.*;

@RestController
@RequestMapping("/api/item-claims")
public class ItemClaimController {
    private final ItemClaimService s;
    public ItemClaimController(ItemClaimService s) { this.s=s; }
    @GetMapping public List<ItemClaim> all() { return s.all(); }
    @GetMapping("/{id}") public ItemClaim get(@PathVariable long id) { return s.get(id); }
    @PostMapping public ResponseEntity<ItemClaim> create(@RequestBody Map<String,Object> b) { return ResponseEntity.status(201).body(s.create(num(b,"lossReportId"),num(b,"foundItemId"),str(b,"evidence"),str(b,"status"))); }
    @PatchMapping("/{id}") public ItemClaim update(@PathVariable long id, @RequestBody Map<String,Object> b) { return s.update(id,num(b,"lossReportId"),num(b,"foundItemId"),str(b,"evidence"),str(b,"status")); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable long id) { s.delete(id); return ResponseEntity.noContent().build(); }
    private String str(Map<String,Object> b,String k) { Object v=b.get(k); return v==null?null:String.valueOf(v); }
    private Long num(Map<String,Object> b,String k) { Object v=b.get(k); if(v==null)return null; try { return Long.valueOf(String.valueOf(v)); } catch(Exception e) { throw new ru.student.lostfound.service.BadRequestException(k+" должен быть числом"); } }
    private Boolean bool(Map<String,Object> b,String k) { Object v=b.get(k); if(v==null)return null; if(v instanceof Boolean x)return x; return Boolean.valueOf(String.valueOf(v)); }
}
