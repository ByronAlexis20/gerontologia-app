package ec.gerontologia.modelo;

import java.util.List;

import javax.persistence.Query;

public class PeriodoLectivoDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<PeriodoLectivo> getPeriodoCentro(Integer idCentro) {
		List<PeriodoLectivo> resultado; 
		Query query = getEntityManager().createNamedQuery("PeriodoLectivo.buscarPorCentro");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idCentro", idCentro);
		resultado = (List<PeriodoLectivo>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<PeriodoLectivo> getPeriodoCentroEnProceso(Integer idCentro) {
		List<PeriodoLectivo> resultado; 
		Query query = getEntityManager().createNamedQuery("PeriodoLectivo.enProceso");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idCentro", idCentro);
		resultado = (List<PeriodoLectivo>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<PeriodoLectivo> getPeriodoCentroDesc(Integer idCentro) {
		List<PeriodoLectivo> resultado; 
		Query query = getEntityManager().createNamedQuery("PeriodoLectivo.buscarPorCentroDesc");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idCentro", idCentro);
		resultado = (List<PeriodoLectivo>) query.getResultList();
		return resultado;
	}
}
