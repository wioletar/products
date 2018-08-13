package pl.akademiakodu.shop.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.akademiakodu.shop.model.Product;
import pl.akademiakodu.shop.repository.ProductRepository;

@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
// to samo co:
// productRepository = new ProductStaticRepository();

    @GetMapping("/products/add")
    public String add() {
        return "products/add";
    }

    @PostMapping("/products/create")
    public String create(@ModelAttribute Product product) {
        productRepository.add(product);
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String index(ModelMap modelMap) {
        modelMap.put("products", productRepository.findAll());
        return "products/index";
    }


    @GetMapping("/products/{name}")
    public String details(@PathVariable String name, ModelMap modelMap) {
        try {
            Product product = productRepository.findProductByName(name);
            modelMap.put("product",product);
        } catch (ProductNotFoundException e) {
            modelMap.put("message","Nie znaleziono produktu");
        }
        return "products/details";
    }

    @GetMapping("/products/{name}/delete")
    public String delete(@PathVariable String name, RedirectAttributes redirectAttributes){
        try {
            productRepository.removeProductByName(name);
            redirectAttributes.addFlashAttribute("message","Product "+name+" succesfully deleted");
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message","Product was not found");
        }
        finally {
            return "redirect:/products";
        }
    }

    @GetMapping("/products/{name}/edit")
    public String edit(@PathVariable String name, ModelMap modelMap){
        try {
            Product product = productRepository.findProductByName(name);
            modelMap.put("product",product);
            } catch (ProductNotFoundException e) {
            modelMap.put("message","Product was not found");
        }
            return "products/edit";
        }
    @PostMapping("/products/{name}/update")
    public String update(@PathVariable String name, @ModelAttribute Product newProduct, ModelMap modelMap) {
        try {
            productRepository.findProductByName(name);
            productRepository.removeProductByName(name);
            productRepository.add(newProduct);
        } catch (ProductNotFoundException e) {
            modelMap.put("message", "Product was not found");
        } finally {
            return "redirect:/products";
        }
    }
}

