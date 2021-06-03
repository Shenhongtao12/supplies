package com.sht.supplies;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.sht.supplies.entity.InStock;
import org.junit.jupiter.api.Test;
import org.springframework.util.Base64Utils;

import java.time.LocalDate;

/**
 * @author Aaron
 * @date 2021/5/9 17:53
 */
public class Base64Test {

    @Test
    public void decode() {
        System.out.println(new String(Base64Utils.decode("MTIzNA==".getBytes())));
    }
    @Test
    public void aes() {
        // 随机生成密钥
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
        System.out.println(key.length);
        System.out.println(new String(key));

        // 构建
        AES aes = SecureUtil.aes("kindy-abc-oopafa".getBytes());

        // 加密
        byte[] encrypt = aes.encrypt("123456");
        System.out.println(new String(encrypt));
        // 解密
        byte[] decrypt = aes.decrypt(encrypt);
        System.out.println(new String(decrypt));

        System.out.println(DigestUtil.md5Hex("123456"));
    }

    @Test
    public void test() {
        /*LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);
        System.out.println(startDate);*/
        System.out.println(DigestUtil.md5Hex("1234567"));
    }

    @Test
    public void eqTest() {
        InStock inStock = new InStock();
        inStock.setRemark(null);
        inStock.setGoodsId(123);
        System.out.println("123".equals(inStock.getRemark()));
    }
}
