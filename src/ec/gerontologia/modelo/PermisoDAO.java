package ec.gerontologia.modelo;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class PermisoDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Permiso> getPermisoById(Integer idMenu) {
		List<Permiso> resultado = new ArrayList<Permiso>(); 
		Query query = getEntityManager().createNamedQuery("Permiso.buscarPorId");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idMenu", idMenu);
		resultado = (List<Permiso>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Permiso> getListaPermisosPadre(Integer idPerfil) {
		List<Permiso> resultado = new ArrayList<Permiso>(); 
		Query query = getEntityManager().createNamedQuery("Permiso.buscarPadrePorPerfil");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idperfil", idPerfil);
		resultado = (List<Permiso>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Permiso> getListaPermisosTodos(Integer idPerfil) {
		List<Permiso> resultado = new ArrayList<Permiso>(); 
		Query query = getEntityManager().createNamedQuery("Permiso.buscarTodosPorPerfil");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idperfil", idPerfil);
		resultado = (List<Permiso>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Permiso> getPermisosAsignados(Integer idPerfil) {
		List<Permiso> resultado = new ArrayList<Permiso>(); 
		Query query = getEntityManager().createNamedQuery("Permiso.buscarPermisoPerfilHijos");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idperfil", idPerfil);
		resultado = (List<Permiso>) query.getResultList();
		return resultado;
	}
	
	public Permiso getPermisoPerfilMenu(Integer idPerfil,Integer idMenu) {
		try {
			Query query = getEntityManager().createNamedQuery("Permiso.buscarPerfilMenu");
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			query.setParameter("idPerfil", idPerfil);
			query.setParameter("idMenu", idMenu);
			return (Permiso) query.getSingleResult();
		}catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
