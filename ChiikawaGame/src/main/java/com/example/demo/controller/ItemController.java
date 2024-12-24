package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.BrandService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ItemService;
import com.example.demo.model.Brand;
import com.example.demo.model.BrandRepository;
import com.example.demo.model.Category;
import com.example.demo.model.CategoryRepository;
import com.example.demo.model.Item;
import com.example.demo.model.ItemOption;
import com.example.demo.model.ItemPhoto;
import com.example.demo.model.ItemPhotoRepositry;
import com.example.demo.model.ItemRepository;
import com.example.demo.model.ItemTransportation;
import com.example.demo.model.UserInfo;
import com.example.demo.model.UserInfoRepository;
import com.example.demo.model.LoginBean;
import com.example.demo.model.TransportationRepository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

@Controller
public class ItemController {
	
	@Autowired
	private ItemRepository itemRepo;
	
    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private TransportationRepository transportationRepo;
    @Autowired
    private ItemPhotoRepositry itemPhotoRepo;
    @Autowired
    private UserInfoRepository userInfoRepo;
    

    // 顯示商品列表
    @GetMapping("/item/itemList")
    public String itemList(Model model) {
        List<Item> itemList = itemService.findAllItem();
        model.addAttribute("itemList", itemList);
        model.addAttribute("categoryList", categoryService.findAll());
        model.addAttribute("brandList", brandService.findAll());
        return "/item/itemListView";
    }

    // 刪除商品
    @GetMapping("/item/deleteItem")
    public String deleteItem(@RequestParam Integer id) {
        try {
            itemService.deleteItemById(id);
            return "redirect:/item/itemList";
        } catch (Exception e) {
            System.out.println("刪除商品失敗: " + e.getMessage());
            return "errorPage";
        }
    }

    // 顯示新增商品頁面
    @GetMapping("/item/addItem")
    public String addItem(Model model) {
        model.addAttribute("item", new Item());
        model.addAttribute("categoryList", categoryService.findAll());
        model.addAttribute("brandList", brandService.findAll());
        model.addAttribute("transportationList", transportationRepo.findAll());
        return "/item/itemAddView";
    }

    // 新增商品
    @PostMapping("/item/add")
    public String addItem(
            @ModelAttribute Item item,
            @RequestParam(required = false) List<Integer> transportationMethods,
            @RequestParam(name = "files", required = false) MultipartFile[] files) throws IOException {

        // 呼叫 Service 處理新增邏輯
        itemService.addItem(item, transportationMethods, files);
        return "redirect:/item/itemList";
    }

    // 顯示編輯商品頁面
    @GetMapping("/item/editItem")
    public String editItem(@RequestParam Integer id, Model model) {
        Item item = itemService.findItemById(id);
        List<ItemPhoto> sortedPhotos = itemPhotoRepo.findByItem_ItemIdOrderBySortOrderAsc(id);
        for(ItemPhoto i : sortedPhotos) {
        System.out.println(i.getId());
        }
        // 將圖片轉換為 Base64 格式
        List<String> base64Photos = sortedPhotos.stream()
                .map(photo -> "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photo.getPhotoFile()))
                .collect(Collectors.toList());
 
        model.addAttribute("item", item);
        model.addAttribute("base64Photos", base64Photos);
        model.addAttribute("categoryList", categoryService.findAll());
        model.addAttribute("brandList", brandService.findAll());
        model.addAttribute("transportationList", transportationRepo.findAll());
        return "/item/itemEditView";
    }


    // 編輯商品
    @PostMapping("/item/editItemPost")
    public String editItemPost(
            @ModelAttribute Item item,
            @RequestParam(required = false) List<Integer> transportationMethods,
            @RequestParam(name = "files", required = false) MultipartFile[] files) throws IOException {
    	
    	
        // 呼叫 Service 處理編輯邏輯
        itemService.updateItem(item, transportationMethods, files);
        return "redirect:/item/itemList";
    }
    //顯示圖片
	@GetMapping("/item/photo")
	public ResponseEntity<?> downloadItemPhoto(@RequestParam Integer id) {
		Optional<ItemPhoto> op = itemPhotoRepo.findById(id);
		
		if(op.isEmpty()) {
//			return new ResponseEntity<>(HttpStatusCode.valueOf(404));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		ItemPhoto itemPhoto = op.get();
		byte[] image = itemPhoto.getPhotoFile();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		
		                          // body , header  , http status code
		return new ResponseEntity<byte[]>(image, headers, HttpStatus.OK);
	}
	//回傳商品多個圖片照順序
	@ResponseBody
	@GetMapping("/api/item/itemPhoto")
	public List<ItemPhoto> findItemPhotoByIdByOrder(@RequestParam int id){
		return itemPhotoRepo.findByItem_ItemIdOrderBySortOrderAsc(id);
	}
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	 使用者前台       %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    // 顯示商品列表
    @GetMapping("/memberCenter/item/itemList")
    public String memberCenteItemList(Model model, HttpSession session) {
        //LoginBean user = (LoginBean) session.getAttribute("user");
        UserInfo user = userInfoRepo.getById(1);
        
    	
        List<Item> itemList = itemService.findAllActiveItemOwnByUserId(user.getUserId());
        model.addAttribute("userId",user.getUserId());
        model.addAttribute("itemList", itemList);
        model.addAttribute("categoryList", categoryService.findAll());
        model.addAttribute("brandList", brandService.findAll());
        return "/item/userItemListView";
    }

    // 刪除商品
    @GetMapping("/memberCenter/item/deleteItem")
    public String memberCenteDeleteItem(@RequestParam Integer id) {
        try {
            Item item=itemService.findItemById(id);
            item.setItemDeleteStatus(true);
            itemRepo.save(item);
            return "redirect:/memberCenter/item/itemList";
        } catch (Exception e) {
            System.out.println("刪除商品失敗: " + e.getMessage());
            return "errorPage";
        }
    }

    // 顯示新增商品頁面
    @GetMapping("/memberCenter/item/addItem")
    public String memberCenteAddItem(Model model, HttpSession session) {
//        LoginBean user = (LoginBean) session.getAttribute("user");
        UserInfo user = userInfoRepo.getById(1);
        model.addAttribute("userId",user.getUserId());
        model.addAttribute("item", new Item());
        model.addAttribute("categoryList", categoryService.findAll());
        model.addAttribute("brandList", brandService.findAll());
        model.addAttribute("transportationList", transportationRepo.findAll());
        return "/item/userItemAddView";
    }

    // 新增商品
    @PostMapping("/memberCenter/item/add")
    public String memberCenteAddItemPost(
            @ModelAttribute Item item,
            @RequestParam(required = false) List<Integer> transportationMethods,
            @RequestParam(name = "files", required = false) MultipartFile[] files) throws IOException {

        // 呼叫 Service 處理新增邏輯
        itemService.addItem(item, transportationMethods, files);
        return "redirect:/memberCenter/item/itemList";
    }

    // 顯示編輯商品頁面
    @GetMapping("/memberCenter/item/editItem")
    public String memberCenteEditItem(@RequestParam Integer id, Model model, HttpSession session) {
        Item item = itemService.findItemById(id);

        LoginBean user = (LoginBean) session.getAttribute("user");
        List<ItemPhoto> sortedPhotos = itemPhotoRepo.findByItem_ItemIdOrderBySortOrderAsc(id);
        for(ItemPhoto i : sortedPhotos) {
        System.out.println(i.getId());
        }
        // 將圖片轉換為 Base64 格式
        List<String> base64Photos = sortedPhotos.stream()
                .map(photo -> "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photo.getPhotoFile()))
                .collect(Collectors.toList());
 
        model.addAttribute("item", item);
        model.addAttribute("userId",user.getUserId());
        model.addAttribute("base64Photos", base64Photos);
        model.addAttribute("categoryList", categoryService.findAll());
        model.addAttribute("brandList", brandService.findAll());
        model.addAttribute("transportationList", transportationRepo.findAll());
        return "/item/userItemEditView";
    }


    // 編輯商品
    @PostMapping("/memberCenter/item/editItemPost")
    public String memberCenteeditItemPost(
            @ModelAttribute Item item,
            @RequestParam(required = false) List<Integer> transportationMethods,
            @RequestParam(name = "files", required = false) MultipartFile[] files) throws IOException {
    	
    	
        // 呼叫 Service 處理編輯邏輯
        itemService.updateItem(item, transportationMethods, files);
        return "redirect:/memberCenter/item/itemList";
    }
	

}