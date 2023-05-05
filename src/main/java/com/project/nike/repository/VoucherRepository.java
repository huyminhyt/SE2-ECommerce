package com.project.nike.repository;

import com.project.nike.dto.VoucherDto;
import com.project.nike.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    // List<VoucherDto> findById(long id);
    void deleteVoucherById(Long id);
    List<VoucherDto> findVoucherByCode(String code);
}
