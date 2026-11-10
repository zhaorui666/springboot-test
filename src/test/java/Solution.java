import java.util.*;

public class Solution {

	/**
	 * 对用户列表进行排序，排序规则：先按照 name 字段的自然顺序排序，name 相同再按照 id 由小到大进行排序
	 *
	 * @param userList 用户列表
	 * @return 排序好的用户列表
	 */
	public static List<User> sortUser(List<User> userList) {
		// 请实现该方法
		Collections.sort(userList);
		return userList;
	}

	public static void main(String[] args) {
		List<User> userList = new ArrayList<User>(){{
			add(new User(3, "Helen"));
			add(new User(1, "Fiona"));
			add(new User(2, "Fiona"));
			add(new User(4, "Annie"));
		}};

		sortUser(userList);
		System.out.println(userList);
	}
}


class User implements Comparable<User> {

	@Override
	public int compareTo(User o) {
		int nameCampareRes = this.getName().compareTo(o.getName());
		if (nameCampareRes != 0) {
			return nameCampareRes;
		}

		return this.getId().compareTo(o.getId());
	}

	Integer id;
	String name;

	public User(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "User{" + "id=" + id + ", name='" + name + '\'' + '}';
	}

}
