package ru.student.lostfound.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.student.lostfound.model.User;
import ru.student.lostfound.service.UserService;
import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService s;
    public UserController(UserService s) { this.s=s; }
    @GetMapping public List<User> all() { return s.all(); }
    @GetMapping("/{id}") public User get(@PathVariable long id) { return s.get(id); }
    @PostMapping public ResponseEntity<User> create(@RequestBody Map<String,Object> b) { return ResponseEntity.status(201).body(s.create(str(b,"name"),str(b,"email"))); }
    @PatchMapping("/{id}") public User update(@PathVariable long id, @RequestBody Map<String,Object> b) { return s.update(id,str(b,"name"),str(b,"email")); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable long id) { s.delete(id); return ResponseEntity.noContent().build(); }
    private String str(Map<String,Object> b,String k) { Object v=b.get(k); return v==null?null:String.valueOf(v); }
    private Long num(Map<String,Object> b,String k) { Object v=b.get(k); if(v==null)return null; try { return Long.valueOf(String.valueOf(v)); } catch(Exception e) { throw new ru.student.lostfound.service.BadRequestException(k+" должен быть числом"); } }
    private Boolean bool(Map<String,Object> b,String k) { Object v=b.get(k); if(v==null)return null; if(v instanceof Boolean x)return x; return Boolean.valueOf(String.valueOf(v)); }
}
