package ru.student.lostfound.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.student.lostfound.model.FoundItem;
import ru.student.lostfound.service.FoundItemService;
import java.util.*;

@RestController
@RequestMapping("/api/found-items")
public class FoundItemController {
    private final FoundItemService s;
    public FoundItemController(FoundItemService s) { this.s=s; }
    @GetMapping public List<FoundItem> all() { return s.all(); }
    @GetMapping("/{id}") public FoundItem get(@PathVariable long id) { return s.get(id); }
    @PostMapping public ResponseEntity<FoundItem> create(@RequestBody Map<String,Object> b) { return ResponseEntity.status(201).body(s.create(str(b,"description"),str(b,"category"),str(b,"foundLocation"),str(b,"status"))); }
    @PatchMapping("/{id}") public FoundItem update(@PathVariable long id, @RequestBody Map<String,Object> b) { return s.update(id,str(b,"description"),str(b,"category"),str(b,"foundLocation"),str(b,"status")); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable long id) { s.delete(id); return ResponseEntity.noContent().build(); }
    private String str(Map<String,Object> b,String k) { Object v=b.get(k); return v==null?null:String.valueOf(v); }
    private Long num(Map<String,Object> b,String k) { Object v=b.get(k); if(v==null)return null; try { return Long.valueOf(String.valueOf(v)); } catch(Exception e) { throw new ru.student.lostfound.service.BadRequestException(k+" должен быть числом"); } }
    private Boolean bool(Map<String,Object> b,String k) { Object v=b.get(k); if(v==null)return null; if(v instanceof Boolean x)return x; return Boolean.valueOf(String.valueOf(v)); }
}
