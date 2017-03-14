package com.microsoft.azuresample.javawebapp.controller;

import com.microsoft.azuresample.javawebapp.model.ToDoComment;
import com.microsoft.azuresample.javawebapp.model.ToDoCommentDAO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by vazvadsk on 2017-01-04.
 */
@RestController
public class CommentController {

    private static final Logger logger = LogManager.getLogger(CommentController.class);

    ToDoCommentDAO dao=new ToDoCommentDAO();

    @RequestMapping(value = "/api/ToDoComments", method = { RequestMethod.POST })
    public
    @ResponseBody
    ToDoComment insertToDoes(@RequestBody ToDoComment item) {
        logger.info("/api/ToDoComments (POST) called");
        return dao.create(item);
    }

    @RequestMapping(value = "/api/ToDoComments", method = { RequestMethod.GET })
    public
    @ResponseBody
    List<ToDoComment> queryToDoes(@RequestParam String gid) {
        logger.info("/api/ToDoComments (GET) called, guid: " + gid);
        return dao.query(gid);
    }
}
