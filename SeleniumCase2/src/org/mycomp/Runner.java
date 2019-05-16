package org.mycomp;

import java.util.ArrayList;
import java.util.List;

import org.mycomp.dao.GoodDAO;
import org.mycomp.model.Good;
import org.mycomp.service.FileWriterService;
import org.mycomp.service.Parser;

public class Runner {

	public static void main(String[] args) {
		Parser parser = new Parser();
		List<Good> goods = new ArrayList();
		goods = parser.selectListGoods("Товары для дома", "Бытовая химия", "Средства для стирки", "Стиральные средства", 5);
		
		GoodDAO goodDao = new GoodDAO();
		for (Good good : goods) {
			goodDao.saveGood(good);
		}
	}
}
