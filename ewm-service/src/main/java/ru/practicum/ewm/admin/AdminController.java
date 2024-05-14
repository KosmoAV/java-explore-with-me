package ru.practicum.ewm.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comments.dto.CommentDto;
import ru.practicum.ewm.comments.dto.UpdateCommentDto;
import ru.practicum.ewm.comments.interfaces.CommentService;
import ru.practicum.ewm.compilations.interfaces.CompilationService;
import ru.practicum.ewm.events.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.dto.NewCategoryDto;
import ru.practicum.ewm.categories.interfaces.CategoryService;
import ru.practicum.ewm.compilations.dto.CompilationDto;
import ru.practicum.ewm.compilations.dto.NewCompilationDto;
import ru.practicum.ewm.compilations.dto.UpdateCompilationRequest;
import ru.practicum.ewm.events.dto.EventFullDto;
import ru.practicum.ewm.events.interfaces.EventService;
import ru.practicum.ewm.events.model.State;
import ru.practicum.ewm.users.dto.NewUserRequest;
import ru.practicum.ewm.users.dto.UserDto;
import ru.practicum.ewm.users.interfaces.UserService;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.Configuration.DATE_TIME_FORMAT;

@Slf4j
@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final CategoryService categoryService;
    private final UserService userService;
    private final EventService eventService;
    private final CompilationService compilationService;
    private final CommentService commentService;

    @PostMapping(value = "/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody @Validated NewCategoryDto newCategoryDto) {

        log.info("Call 'addCategory': {}", newCategoryDto);

        return categoryService.addCategory(newCategoryDto);
    }

    @DeleteMapping(value = "/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @Positive Long catId) {

        log.info("Call 'deleteCategory': id = {}", catId);

        categoryService.deleteCategory(catId);
    }

    @PatchMapping(value = "/categories/{catId}")
    public CategoryDto updateCategory(@RequestBody @Validated CategoryDto categoryDto,
                                      @PathVariable @Positive Long catId) {

        log.info("Call 'updateCategory': {}, id = {}", categoryDto, catId);

        return categoryService.updateCategory(categoryDto, catId);
    }

    @GetMapping(value = "/events")
    public List<EventFullDto> getEventsByFilter(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<State> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {

        log.info("Call 'getEventsByFilter': users = {}, states = {}, categories = {}, rangeStart = {}, " +
                        "rangeEnd = {}, from = {}, size = {}", users, states, categories,
                rangeStart, rangeEnd, from, size);

        return eventService.getEventsAdminByFilter(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping(value = "/events/{eventId}")
    public EventFullDto updateEventByAdmin(@RequestBody @Validated UpdateEventAdminRequest updateEventAdminRequest,
                                           @PathVariable @Positive Long eventId) {

        log.info("Call 'updateEventByAdmin': {}, eventId = {}", updateEventAdminRequest, eventId);

        return eventService.updateEventByAdmin(updateEventAdminRequest, eventId);
    }

    @GetMapping(value = "/users")
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                  @RequestParam(defaultValue = "10") @Positive Integer size) {

        log.info("Call 'getUsers': ids = {}, from = {}, size = {}", ids, from, size);

        return userService.getUsers(ids, from, size);
    }

    @PostMapping(value = "/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody @Validated NewUserRequest newUserRequest) {

        log.info("Call 'addUser': {}", newUserRequest);

        return userService.addUser(newUserRequest);
    }

    @DeleteMapping(value = "/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Validated @PathVariable @Positive Long userId) {

        log.info("Call 'deleteUser': userId = {}", userId);

        userService.deleteUser(userId);
    }

    @PostMapping(value = "/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody @Validated NewCompilationDto newCompilationDto) {

        log.info("Call 'addCompilation': {}", newCompilationDto);

        return compilationService.addCompilation(newCompilationDto);
    }

    @DeleteMapping(value = "/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @Positive Long compId) {

        log.info("Call 'deleteCompilation': compId = {}", compId);

        compilationService.deleteCompilation(compId);
    }

    @PatchMapping(value = "/compilations/{compId}")
    public CompilationDto updateCompilation(@RequestBody @Validated UpdateCompilationRequest updateCompilationRequest,
                                            @PathVariable @Positive Long compId) {

        log.info("Call 'updateCompilation': {}, compId = {}", updateCompilationRequest, compId);

        return compilationService.updateCompilation(updateCompilationRequest, compId);
    }

    @PatchMapping(value = "/comments/{commentId}")
    public CommentDto updateComment(@RequestBody @Validated UpdateCommentDto updateCommentDto,
                                    @PathVariable @Positive Long commentId) {

        log.info("Call 'updateComment': {}, commentId = {}", updateCommentDto, commentId);

        return commentService.updateCommentByAdmin(updateCommentDto, commentId);
    }

    @DeleteMapping(value = "/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @Positive Long commentId) {

        log.info("Call 'deleteComment': commentId = {}", commentId);

        commentService.deleteCommentByAdmin(commentId);
    }
}
