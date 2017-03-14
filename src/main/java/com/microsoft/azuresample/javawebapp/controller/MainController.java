package com.microsoft.azuresample.javawebapp.controller;

import com.microsoft.azuresample.javawebapp.JavaWebApp;
import com.microsoft.azuresample.javawebapp.esb.TopicHelper;
import com.microsoft.azuresample.javawebapp.model.ToDo;
import com.microsoft.azuresample.javawebapp.model.ToDoComment;
import com.microsoft.azuresample.javawebapp.model.ToDoCommentDAO;
import com.microsoft.azuresample.javawebapp.model.ToDoDAO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class MainController {

    private static final Logger logger = LogManager.getLogger(MainController.class);

    ToDoDAO dao=new ToDoDAO();

    @RequestMapping(value = "/api/ToDoes", method = { RequestMethod.POST })
    public
    @ResponseBody
    ToDo insertToDoes(@RequestBody ToDo item) {
        logger.info("/api/ToDoes (POST) called");
        // create ToDo item
        ToDo ret = dao.create(item);

        // create automatic comment
        ToDoCommentDAO cdao = new ToDoCommentDAO();
        ToDoComment comm = new ToDoComment();
        comm.setCreated(LocalDateTime.now().toString());
        comm.setTodoGid(ret.getGid());
        comm.setComment("Automatically created comment during item creation at " + LocalDateTime.now().toString());
        cdao.create(comm);

        // send item to service bus
        TopicHelper th = new TopicHelper();
        th.sendToDo(ret);

        return ret;
    }

    @RequestMapping(value = "/api/ToDoes", method = { RequestMethod.GET })
    public
    @ResponseBody
    List<ToDo> queryToDoes() {
        logger.info("/api/ToDoes (GET) called");
        return dao.query();
    }

    @RequestMapping(value = "/api/Test", method = { RequestMethod.GET })
    public
    @ResponseBody
    String testToDoes() {

        return dao.test();
    }
}

