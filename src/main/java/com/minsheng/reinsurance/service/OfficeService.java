package com.minsheng.reinsurance.service;


import com.minsheng.reinsurance.bean.entity.Office;
import com.minsheng.reinsurance.bean.view.OfficeView;
import com.minsheng.reinsurance.dao.OfficeDao;
import com.minsheng.reinsurance.enums.DeleteFlagEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by panwei on 16/10/17.
 */
@Service
public class OfficeService extends Services<Office> {
    @Autowired
    OfficeDao officeDao;

    public List<OfficeView> findAllOffice(HashMap<String, Object> params) {
        return officeDao.findAllOffice(params);
    }

    public void getOfficeTreeTable(List<OfficeView> officeTreeTable, List<OfficeView> allOffice, Integer parentId) {
        for (OfficeView parent : allOffice) {
            if (parent.getParentId().equals(parentId)) {
                officeTreeTable.add(parent);
                for (OfficeView child : allOffice) {
                    if (child.getParentId().equals(parent.getId())) {
                        getOfficeTreeTable(officeTreeTable, allOffice, parent.getId());
                        break;
                    }
                }
            }
        }
    }

    /**
     * 机构树
     *
     * @param parentId:父id
     * @return
     */
    public List<HashMap<String, Object>> getAllOffice(Integer parentId) {
        List<HashMap<String, Object>> offices = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("delFlag", DeleteFlagEnum.NORMAL.getCode());
        if (parentId != null) {
            params.put("parentId", parentId);
        }

        List<Office> items = officeDao.findBy(params);
        for (Office item : items) {
            HashMap<String, Object> m = new HashMap<>();
            Integer id = item.getId();
            m.put("id", id);
            m.put("name", item.getName());
            m.put("open", true);
            List<HashMap<String, Object>> child = getAllOffice(item.getId());
            m.put("children", child.size() > 0 ? child : null);
            offices.add(m);
        }
        return offices;
    }

    public boolean validateOfficeParams(HashMap<String, Object> rtn, Office office) {
        if (office != null) {
            String name = office.getName();
            String master = office.getMaster();
            String email = office.getEmail();
            Integer parentId = office.getParentId();
            Integer sort = office.getSort();
            Integer useable = office.getUseable();
            if (StringUtils.isBlank(name) || StringUtils.isBlank(master) || StringUtils.isBlank(email)
                    || parentId == null || sort == null || useable == null) {
                rtn.put("msg", "请填写完整");
                return false;
            }
            HashMap<String, Object> params = new HashMap<>();
            params.put("name", name);
            List<Office> officeList = officeDao.findBy(params);
            Integer id = office.getId();
            if (id == null) { // add
                if (officeList.size() > 0) {
                    rtn.put("msg", "机构名称已存在");
                    return false;
                }
            } else { //update
                if (officeList.size() > 0) {
                    if (!Objects.equals(id, officeList.get(0).getId())) {
                        rtn.put("msg", "机构名称已存在");
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public Integer getDepth(Integer officeId) {
        Integer depth = 0;
        if (officeId != null) {
            depth = 1;
            for (int i = 0; i < 5; i++) {
                Integer parentId = officeDao.getEntityById(officeId).getParentId();
                if (parentId.equals(0)) {
                    return depth;
                }
                officeId = parentId;
                depth++;
            }
        }
        return depth;
    }
}
