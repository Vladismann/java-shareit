package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repo.BookingRepo;
import ru.practicum.shareit.common.CustomPageRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.CommentRepo;
import ru.practicum.shareit.item.repo.ItemRepo;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepo;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemServiceTest {

    private ItemService itemService;
    private final User userSuccess = new User(1, "Test", "Test@mail.ru");
    private ItemDto itemDto
            = new ItemDto(0, "Test", "TestD", true, null, null, null, null);
    private Item item = new Item(1, "Test", "TestD", true, userSuccess, null);
    private ItemDto itemSuccess;

    private Set<Comment> comments = Set.of(new Comment(1, "Test", item, userSuccess, LocalDateTime.now()));
    private List<Booking> bookings = List.of(new Booking(1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), item, userSuccess, BookingStatus.REJECTED));
    Pageable pageable =  new CustomPageRequest(1, 1, Sort.by("id"));


    @Mock
    private ItemRepo itemRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private CommentRepo commentRepo;
    @Mock
    private BookingRepo bookingRepo;


    @BeforeEach
    void before() {
        itemService = new ItemServiceImpl(itemRepo, userRepo, commentRepo, bookingRepo);
        when(userRepo.existsById(any())).thenReturn(true);
        itemSuccess
                = new ItemDto(1, "Test", "TestD", true, null, null, null, null);
    }

    @Test
    public void createItem() {
        when(userRepo.getReferenceById((long) 1)).thenReturn(userSuccess);
        when(itemRepo.save(any())).thenReturn(item);
        ItemDto createdItem = itemService.createItem(1, itemDto);
        assertEquals(itemSuccess, createdItem);
    }

    @Test
    public void updateItem() {
        when(itemRepo.existsById(any())).thenReturn(true);
        when(userRepo.getReferenceById((long) 1)).thenReturn(userSuccess);
        when(itemRepo.getReferenceById((long) 1)).thenReturn(item);
        when(itemRepo.save(any())).thenReturn(item);
        ItemDto updatedItem = itemService.updateItem(1, 1, itemDto);
        assertEquals(itemSuccess, updatedItem);
    }

    @Test
    public void getItem() {
        when(itemRepo.existsById(any())).thenReturn(true);
        when(itemRepo.getReferenceById((long) 1)).thenReturn(item);
        ItemDto gottenItem = itemService.getItem(1, 1);
        itemSuccess.setComments(new HashSet<>());
        assertEquals(itemSuccess, gottenItem);
    }

    @Test
    public void getUserItems() {
        when(itemRepo.existsById(any())).thenReturn(true);
        when(itemRepo.findByOwnerIdOrderById(any(), any())).thenReturn(List.of(item));
        when(bookingRepo.findAllByItemIdIn(any())).thenReturn(bookings);
        when(commentRepo.findAllByItemIdIn(any())).thenReturn(comments);
        List<ItemDto> gottenItems = itemService.getAllByUserId(1, pageable);
        List<ItemDto> expectedItems = ItemMapper.toItemDtoForOwnerList(List.of(item), bookings, comments);
        assertEquals(expectedItems, gottenItems);
    }

    @Test
    public void getSearchItems() {
        when(itemRepo.search(any(), any())).thenReturn(List.of(item));
        when(bookingRepo.findAllByItemIdIn(any())).thenReturn(bookings);
        when(commentRepo.findAllByItemIdIn(any())).thenReturn(comments);
        List<ItemDto> gottenItems = itemService.searchItemByText("test", pageable);
        List<ItemDto> expectedItems = ItemMapper.toItemDtoForOwnerList(List.of(item), bookings, comments);
        assertEquals(expectedItems, gottenItems);
    }



}
