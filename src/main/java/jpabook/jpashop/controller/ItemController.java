package jpabook.jpashop.controller;

import jpabook.jpashop.Service.ItemService;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private  final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItem();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId);

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("items/{itemId}/edit")
    public String updateItem (@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {
        /**
         * 이 경우 id값을 url 파라미터로 받기 때문에 변형가능성이 높다.
         * 그러므로 수정하는 사람의 권한체크를 하는 것이 좋다.
         * 예) '작성자 == 수정자' 체크
         */
//        Book book = new Book();
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());
//        itemService.saveItem(book);
        /** 이 경우는 "어설프게 Controller에서 Entity를 생성하지 마라"라는 어원 담아,
         * 개선한 코드로 리팩토링 하였습니다.
         */
        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        // 변경감지 형식 및 코드 약간 간소화
        // DTO 같은 것을 하나 만들어서 해주는 것이 더 간소화 될 수 있어서 좋음

        /** 민구의 머지와 변경감지 총 정리 :: Update 주의사항 **
         * 
         * 우선 위와같이 줄였지만, 중요한 요점은 Update 시,
         * 병합(merge)를 쓰지말고, JPA 변경감지를 쓰자!
         *
         * 병합은 모든 데이터를 전부 통째로 바꿔버림 :: 원하는 곳만 수정이 불가능함.
         *
         * 변경감지는 '영속성 데이터'를 set으로 변경할 시
         * JPA가 감지하여 수정된 것을 자동으로 알아차린 뒤 감지한 곳만 데이터를 변경해줌.
         *
         * '영속성'이라는 것을 설명에 넣은 이유는 영속성 컨텍스트에 없다면, JPA가 관리하지 않기때문에
         * 변경감지 또한 알아차릴 수 없다. 무슨말이냐면, JPA는 영속성 컨텍스트 안에서 관리되기 때문에,
         * 영속성 컨텍스트 밖에서 변경된 값은 볼 수가 없어서 모른다는 것이다.
         * 이러함으로 인해, 변경 되었는지 또한 알 수가 없는 것이다.
         */

        return "redirect:/items";
    }

}
