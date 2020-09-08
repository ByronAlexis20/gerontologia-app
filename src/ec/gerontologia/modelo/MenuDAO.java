package ec.gerontologia.modelo;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class MenuDAO extends ClaseDAO{

	@SuppressWarnings("unchecked")
	public List<Menu> getOpciones(){
		List<Menu> opciones = new ArrayList<>();		
		Query query = getEntityManager().createNamedQuery("Menu.findAll");
		opciones = (List<Menu>) query.getResultList();		
		return opciones;
	}
	@SuppressWarnings("unchecked")
	public List<Menu> getOpcionesActivos(){
		List<Menu> opciones = new ArrayList<>();		
		Query query = getEntityManager().createNamedQuery("Menu.buscarActivos");
		opciones = (List<Menu>) query.getResultList();		
		return opciones;
	}
	@SuppressWarnings("unchecked")
	public List<Menu> getListaMenuHijo(Integer idPadre){
		List<Menu> opciones = new ArrayList<>();		
		Query query = getEntityManager().createNamedQuery("Menu.buscarHijos");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPadre", idPadre);
		opciones = (List<Menu>) query.getResultList();		
		return opciones;
	}
	@SuppressWarnings("unchecked")
	public String getMenuPadre(Integer idMenuPadre){
		List<Menu> opciones = new ArrayList<>();		
		Query query = getEntityManager().createNamedQuery("Menu.buscarPadre");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idMenuPadre", idMenuPadre);
		opciones = (List<Menu>) query.getResultList();
		String resultado = "";
		if(opciones.size() > 0) 
			resultado = opciones.get(0).getDescripcion();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Menu> getMenuById(Integer idMenu){
		List<Menu> opciones = new ArrayList<>();		
		Query query = getEntityManager().createNamedQuery("Menu.buscarPorId");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idMenu", idMenu);
		opciones = (List<Menu>) query.getResultList();		
		return opciones;
	}
}
