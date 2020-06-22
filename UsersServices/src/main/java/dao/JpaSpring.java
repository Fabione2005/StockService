package dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import model.User;

@Repository
public interface JpaSpring extends JpaRepository<User, Integer>
{
	User findByUserName(String name);
	User findByEmail(String name);
	User findByLogged(boolean logged);
	
	@Transactional
	@Modifying
	@Query("Delete from User c Where c.userName=:userName")
	void deleteByUserName(@Param("userName")String userName);
	
	@Transactional
	@Modifying
	@Query("Delete from User c Where c.email=:email")
	void deleteByEmail(@Param("email")String email);
	
	@Transactional
	@Modifying
	@Query("Update User c set c.logged =:logged where c.id=:id")
	void LogInOrLogOutUser(@Param("id")int id,@Param("logged")boolean logged);
}
