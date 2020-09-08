package ec.gerontologia.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class MatriculaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Matricula> getPersonaMatricula(String cedula) {
		List<Matricula> resultado = new ArrayList<Matricula>(); 
		Query query = getEntityManager().createNamedQuery("Matricula.buscarPersonaMatricula");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("cedula", cedula);
		resultado = (List<Matricula>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Matricula> getPersonaMatriculados(Integer idPeriodo) {
		List<Matricula> resultado = new ArrayList<Matricula>(); 
		Query query = getEntityManager().createNamedQuery("Matricula.buscarPersonaPorPeriodo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPeriodo", idPeriodo);
		resultado = (List<Matricula>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Matricula> getPersonaMatriculadosCentro(Integer idCentro) {
		List<Matricula> resultado = new ArrayList<Matricula>(); 
		Query query = getEntityManager().createNamedQuery("Matricula.buscarPersonaPorCentro");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idCentro", idCentro);
		resultado = (List<Matricula>) query.getResultList();
		return resultado;
	}
}
