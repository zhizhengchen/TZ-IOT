// package infoauto.controller;
//
// import com.alibaba.fastjson.JSONObject;
// import infoauto.entity.ProductionLineUrl;
// import infoauto.service.IotService;
// import infoauto.service.clutter.ProductionLineUrlService;
// import infoauto.vo.Result;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiImplicitParam;
// import io.swagger.annotations.ApiOperation;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;
//
// import java.util.List;
//
// @RestController
// @RequestMapping
// @Api("分发请求接口接口")
// public class reqController {
//
//     @Autowired
//     private IotService iotService;
//     @Autowired
//     private ProductionLineUrlService productionLineUrlService;
//
//
//     @PostMapping("itoReq")
//     public Result iotGet(@RequestBody JSONObject requestBody) {
//         System.out.println("接收请求的数据：" + requestBody);
//         Result result = iotService.zkSend(requestBody);
//         System.out.println("请求响应的数据：" + result);
//         return result;
//     }
//
//     @ApiOperation(value = "测试", notes = "测试")
//     // @ApiImplicitParam(name = "id",value = "表示id为参数名，dataType为参数类型",dataType = "Long")
//     @RequestMapping("/test")
//     public String test() {
//
//         return "test";
//     }
//
//     @ApiOperation(value = "表查询测试", notes = "数据库测试")
//     @GetMapping("/findAll")
//     public Result selectAll() {
//         List<ProductionLineUrl> list = productionLineUrlService.select();
//         System.out.println(list);
//         return Result.error(list.toString());
//     }
//
//     @ApiOperation(value = "表条件查询测试", notes = "数据库测试")
//     @ApiImplicitParam(name = "productionLineNo",value = "产线编号",dataType = "String")
//     @GetMapping("/findByProductionLineNo")
//     public Result selectByProductionLineNo(String productionLineNo) {
//         List<ProductionLineUrl> plu = productionLineUrlService.selectByProductionLine(productionLineNo);
//         System.out.println(plu);
//         return Result.error(plu.toString());
//     }
//
//     @ApiOperation(value = "表添加测试", notes = "数据库测试")
//     @ApiImplicitParam(name = "plu",value = "对象",dataType = "ProductionLineUrl")
//     @PostMapping("/add")
//     public Result add(@RequestBody ProductionLineUrl plu) {
//         int i = productionLineUrlService.add(plu);
//         return Result.error(i + "");
//     }
//
//     @ApiOperation(value = "表修改测试", notes = "数据库测试")
//     @ApiImplicitParam(name = "plu",value = "对象",dataType = "ProductionLineUrl")
//     @PutMapping("/update")
//     public Result updateByProductionLineNo(@RequestBody ProductionLineUrl plu) {
//         int i = productionLineUrlService.updateTab(plu);
//         return Result.error(i + "");
//     }
//
//     @ApiOperation(value = "表删除测试", notes = "数据库测试")
//     @ApiImplicitParam(name = "productionLineNo",value = "产线编号",dataType = "String")
//     @DeleteMapping("/delete")
//     public Result deleteByProductionLineNo(String productionLineNo) {
//         int i = productionLineUrlService.delete(productionLineNo);
//         return Result.error(i + "");
//     }
// }