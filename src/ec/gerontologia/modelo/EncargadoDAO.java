package ec.gerontologia.modelo;

import java.util.List;

import javax.persistence.Query;

import ec.gerontologia.util.Context;

public class EncargadoDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Encargado> getEncargadosCentro(Integer idCentro) {
		List<Encargado> resultado; 
		Query query = getEntityManager().createNamedQuery("Encargado.buscarEncargadoCentro");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idCentro", idCentro);
		resultado = (List<Encargado>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Encargado> getEncargadosAsignados() {
		List<Encargado> resultado; 
		Query query = getEntityManager().createNamedQuery("Encargado.buscarAsignados");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Encargado>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Encargado> getEncargadosUsuario() {
		List<Encargado> resultado; 
		Query query = getEntityManager().createNamedQuery("Encargado.buscarUsuario");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idUsuario", Context.getInstance().getUsuarioLogeado().getIdUsuario());
		resultado = (List<Encargado>) query.getResultList();
		return resultado;
	}
}
