package jit.gxw.service.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jit.gxw.constant.MessageConstant;
import jit.gxw.constant.TwoStatusConstant;
import jit.gxw.context.BaseContext;
import jit.gxw.dto.ClassificationDTO;
import jit.gxw.dto.ClassificationPageQueryDTO;
import jit.gxw.entity.Classification;
import jit.gxw.entity.Vehicle;
import jit.gxw.exception.DeletionNotAllowedException;
import jit.gxw.exception.StatusNotAllowedException;
import jit.gxw.mapper.ClassificationMapper;
import jit.gxw.mapper.VehicleMapper;
import jit.gxw.result.PageResult;
import jit.gxw.service.ClassificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ClassificationServiceImpl implements ClassificationService {

    @Autowired
    private ClassificationMapper classificationMapper;
    @Autowired
    private VehicleMapper vehicleMapper;

    /**
     * 分类分页查询
     * @param classificationPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(ClassificationPageQueryDTO classificationPageQueryDTO) {
        PageHelper.startPage(classificationPageQueryDTO.getPage(),classificationPageQueryDTO.getPageSize());
        //下一条sql进行分页，自动加入limit关键字分页
        Page<Classification> page = classificationMapper.pageQuery(classificationPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增分类
     * @param classificationDTO
     */
    @Override
    public void save(ClassificationDTO classificationDTO) {
        Classification classification = new Classification();
        BeanUtils.copyProperties(classificationDTO,classification);

        //设置属性
        //默认禁用
        classification.setStatus(TwoStatusConstant.DISABLE);


        classification.setCreateTime(LocalDateTime.now());
        classification.setUpdateTime(LocalDateTime.now());

        classification.setCreateUser(BaseContext.getCurrentId());
        classification.setUpdateUser(BaseContext.getCurrentId());

        classificationMapper.insert(classification);


    }

    /**
     * 修改分类
     * @param classificationDTO
     */
    @Override
    public void update(ClassificationDTO classificationDTO) {
        Classification classification = new Classification();
        BeanUtils.copyProperties(classificationDTO,classification);

        //设置修改信息
        classification.setUpdateTime(LocalDateTime.now());
        classification.setUpdateUser(BaseContext.getCurrentId());

        classificationMapper.update(classification);
    }

    /**
     * 设置分类状态
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        //判断是否有绑定的状态为启用的车辆
        List<Vehicle> vehicles =vehicleMapper.getByClassificationId(id);
        if (vehicles!=null && !vehicles.isEmpty()&&status==1){
            vehicles.forEach(vehicle -> {
                if (vehicle.getStatus()==TwoStatusConstant.ENABLE){
                    throw new StatusNotAllowedException(MessageConstant.CLASSIFICATION_BE_RELATED_BY_VEHICLE_ON_ENABLE);
                }        });
        }




        Classification classification = Classification.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();
        classificationMapper.update(classification);
    }

    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        //查询当前分类是否关联了车辆
        List<Vehicle> vehicles =vehicleMapper.getByClassificationId(id);
        if (vehicles!=null && !vehicles.isEmpty()){
            throw new DeletionNotAllowedException(MessageConstant.CLASSIFICATION_BE_RELATED_BY_VEHICLE);
        }
        //删除分类
        classificationMapper.deleteById(id);

    }

    /**
     * 用户查询分类
     * @return
     */
    @Override
    public List<Classification> list() {
        return classificationMapper.list();
    }
}
