package com.example.blog.controller;

import com.example.blog.Dto.Form;
import com.example.blog.Dto.PostForm;
import com.example.blog.config.AppConfig;
import com.example.blog.data.*;
import com.example.blog.service.RightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.Optional;

@Controller("/")
public class MainController {


    private BCryptPasswordEncoder encoder = AppConfig.passwordEncoder();

    @Autowired
    private RightService rightsService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuthorsRepository authorsRepository;

    @RequestMapping("/")
    public String mainPageRequest(Model model){
        model.addAttribute("authors",authorsRepository.findAll());
        return "main";
    }

    @RequestMapping("/profile")
    public String profileDefaultRequest(RedirectAttributes attr){
        if(rightsService.isUser()){
            String id = rightsService.getAuthor().getId().toString();
            attr.addAttribute("id",id);
            return "redirect:/profile/{id}";
        }else{
            return "redirect:/";
        }
    }

    @RequestMapping("/profile/{id}")
    public String profileRequest(@PathVariable String id, Model model, RedirectAttributes attr){
        if(id == null || id.trim()==""){
            String id2 = rightsService.getAuthor().getId();
            attr.addAttribute("id",id2);
            return "redirect:/profile/{id}";
        }
        if(authorsRepository.findById(id).isPresent()) {
            Author author = authorsRepository.findById(id).get();
            model.addAttribute("author", author);
            model.addAttribute("posts", postRepository.findAllByAuthorId(author.getId()));
            if(rightsService.getAuthor()==null){
                model.addAttribute("error","Nejste přihlášen");
                return "error";
            }
            if (rightsService.getAuthor().equals(author) || rightsService.isAdmin()) {
                return "profile-owner";
            } else {
                return "profile";
            }
        }else{
            model.addAttribute("error","Tento uživatel neexistuje");
            return "error";
        }
    }

    @GetMapping("/login")
    public String loginGetRequest(Model model){
        model.addAttribute("form",new Form());
        return "form-login";
    }

    @GetMapping("register")
    public String registerGetRequest(Model model){
        model.addAttribute("form",new Form());
        return "form-register";
    }

    @PostMapping("/login")
    public String loginPostRequest(@ModelAttribute("form") Form form,RedirectAttributes attr,Model model){
        String username = form.getUsername();
        Optional<Author> author = authorsRepository.findByName(username);
        if(author.isPresent()) {
            if (encoder.matches(form.getPassword(), author.get().getHashedPasswd())) {
                rightsService.setAuthor(author.get());
                if (author.get().getRights() == Rights.ADMIN) {
                    rightsService.setAdmin(true);
                }
                rightsService.setUser(true);
                attr.addAttribute("id",author.get().getId());
                return "redirect:/profile/{id}";
            } else {
                model.addAttribute("error", "Špatné heslo");
                return "error";
            }
        }else{
            model.addAttribute("error", "Tento uživatel neexistuje");
            return "error";
        }

    }
    @PostMapping("register")
    public String registerPostRequest(@ModelAttribute("form") Form form,Model model){
        String hashedPasswd = encoder.encode(form.getPassword());
        String username = form.getUsername();
        if(authorsRepository.existsByName(username)){
            model.addAttribute("error", "Toto jméno je použité");
            return "error";
        }else{
            Author author = new Author();
            author.setHashedPasswd(hashedPasswd);
            author.setName(username);
            author.setRights(Rights.USER);
            authorsRepository.save(author);
            return "redirect:/profile";
        }

    }
    @PostMapping("/post-article")
    public String postArticlePostRequest(@ModelAttribute("postForm") PostForm form) {
        Post post = new Post();
        post.setAuthorId(rightsService.getAuthor().getId());
        post.setDate(new Date());
        post.setContent(form.getText());
        post.setTitle(form.getTitle());
        postRepository.insert(post);
        return "redirect:/profile";
    }
    @GetMapping("/post-article")
    public String postArticleGetRequest(Model model){
        model.addAttribute("postForm", new PostForm());
        return "post-form";
    }


    @RequestMapping("/delete-article/{id}")
    public String deleteRequest(@PathVariable String id,Model model, RedirectAttributes attr){
        if(rightsService.getAuthor().equals(postRepository.findById(id).get().getAuthorId()) || rightsService.isAdmin()){
            String authorID = postRepository.findById(id).get().getAuthorId();
            postRepository.deleteById(id);
            attr.addAttribute("id",authorID);
        }else{
            model.addAttribute("error","Nemáte oprávnění ke smazání příspěvku.");
            return "error";
        }
        return "redirect:/profile/{id}";
    }


    @RequestMapping("/post/{id}")
    public String postRequest(@PathVariable String id, Model model) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            model.addAttribute("post", post.get());
            model.addAttribute("author", authorsRepository.findById(post.get().getAuthorId()).get());
            return "post";
        } else {
            model.addAttribute("error", "Tento příspěvek neexistuje");
            return "error";
        }
    }

    @RequestMapping("/search")
    public String searchRequest(@RequestParam("searchField") String search, Model model){
        model.addAttribute("authors",authorsRepository.findByNameStartsWith(search));
        return "search";
    }








}
