package com.microsoft.azuresample.javawebapp.controller;

import com.microsoft.azuresample.javawebapp.model.ToDoComment;
import com.microsoft.azuresample.javawebapp.model.ToDoCommentDAO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by vazvadsk on 2017-01-04.
 */
@RestController
public class CommentController {

    ToDoCommentDAO dao=new ToDoCommentDAO();

    @RequestMapping(value = "/api/ToDoComments", method = { RequestMethod.POST })
    public
    @ResponseBody
    ToDoComment insertToDoes(@RequestBody ToDoComment item) {
        return dao.create(item);
    }

    @RequestMapping(value = "/api/ToDoComments", method = { RequestMethod.GET })
    public
    @ResponseBody
    List<ToDoComment> queryToDoes(@RequestParam String gid) {
        return dao.query(gid);
    }
}
