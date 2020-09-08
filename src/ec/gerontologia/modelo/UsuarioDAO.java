package ec.gerontologia.modelo;
import javax.persistence.Query;

import ec.gerontologia.util.Constantes;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends ClaseDAO{
	
	@SuppressWarnings("unchecked")
	public List<Usuario> getListaUsuariosActivos(String value) {
		List<Usuario> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Usuario.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Usuario>) query.getResultList();
		return resultado;
	}
	
	public Usuario getUsuario(String nombreUsuario) {
		Usuario usuario; 
		Query consulta;
		consulta = getEntityManager().createNamedQuery("Usuario.buscaUsuario");
		consulta.setParameter("nombreUsuario", nombreUsuario);
		usuario = (Usuario) consulta.getSingleResult();
		return usuario;
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> getListaUsuario(String value) {
		List<Usuario> resultado = new ArrayList<Usuario>(); 
		String patron = value;
		Query query = getEntityManager().createNamedQuery("Usuario.buscarPorCedula");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Usuario>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Usuario> getListausuarioBuscar(String value) {
		List<Usuario> resultado = new ArrayList<Usuario>(); 
		Query query = getEntityManager().createNamedQuery("Usuario.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron","%" + value.toLowerCase() + "%");
		resultado = (List<Usuario>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Usuario> getBuscarUsuario(String value,Integer idUsuario) {
		List<Usuario> resultado = new ArrayList<Usuario>(); 
		Query query = getEntityManager().createNamedQuery("Usuario.buscarPorUsuario");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("usuario", value.toLowerCase());
		query.setParameter("idUsuario", idUsuario);
		resultado = (List<Usuario>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> getListaUsuariosActivosTecnicos(String value) {
		List<Usuario> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Usuario.buscarPorPatronTecnico");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		query.setParameter("idTipoUsuario", Constantes.ID_USUARIO_ADMIN);
		resultado = (List<Usuario>) query.getResultList();
		return resultado;
	}
}
