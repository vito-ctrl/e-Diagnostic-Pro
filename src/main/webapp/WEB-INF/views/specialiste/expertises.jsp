<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Demandes d'expertise - Médecin Spécialiste" />
</jsp:include>

<div class="space-y-6">
    <!-- Header with Filters -->
    <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-4">
        <div class="sm:flex sm:items-center sm:justify-between">
            <div>
                <h1 class="text-2xl font-bold text-slate-900">US7 : Demandes de Télé-Expertise reçues</h1>
                <p class="mt-1 text-sm text-slate-500">
                    Filtrage dynamique via Java Stream API par Statut et Niveau de Priorité
                </p>
            </div>
            <div class="mt-3 sm:mt-0 flex items-center space-x-2">
                <span class="px-3 py-1 bg-purple-100 text-purple-800 rounded-full text-xs font-bold border border-purple-200">
                    ${demandes.size()} demande(s) listée(s)
                </span>
            </div>
        </div>

        <!-- Filter Bar -->
        <form action="${pageContext.request.contextPath}/specialiste/expertises" method="GET" class="pt-2 border-t border-slate-100 flex flex-wrap gap-3 items-center">
            <div>
                <label class="block text-[11px] font-bold uppercase text-slate-500 mb-1">Statut :</label>
                <select name="statut" class="px-3 py-2 border border-slate-300 rounded-xl text-xs focus:ring-2 focus:ring-purple-500">
                    <option value="">Tous les statuts</option>
                    <c:forEach var="st" items="${statuts}">
                        <option value="${st.name()}" ${selectedStatut == st ? 'selected' : ''}>${st.libelle}</option>
                    </c:forEach>
                </select>
            </div>

            <div>
                <label class="block text-[11px] font-bold uppercase text-slate-500 mb-1">Priorité :</label>
                <select name="priorite" class="px-3 py-2 border border-slate-300 rounded-xl text-xs focus:ring-2 focus:ring-purple-500">
                    <option value="">Toutes les priorités</option>
                    <c:forEach var="pr" items="${priorites}">
                        <option value="${pr.name()}" ${selectedPriorite == pr ? 'selected' : ''}>${pr.libelle}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="pt-5">
                <button type="submit" class="px-4 py-2 bg-purple-600 hover:bg-purple-700 text-white rounded-xl text-xs font-semibold shadow-sm transition">
                    <i class="fa-solid fa-filter mr-1"></i> Filtrer (Stream API)
                </button>
                <a href="${pageContext.request.contextPath}/specialiste/expertises" class="ml-2 px-3 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-xl text-xs font-semibold transition">
                    Réinitialiser
                </a>
            </div>
        </form>
    </div>

    <c:if test="${param.success == 'repondu'}">
        <div class="p-4 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-800 text-sm flex items-center space-x-2">
            <i class="fa-solid fa-circle-check text-emerald-600 text-lg"></i>
            <span>Votre avis médical et vos recommandations ont été transmis avec succès au médecin traitant.</span>
        </div>
    </c:if>

    <!-- Demandes list -->
    <div class="space-y-4">
        <c:choose>
            <c:when test="${empty demandes}">
                <div class="bg-white rounded-2xl border border-slate-200 p-12 text-center text-slate-400">
                    <i class="fa-solid fa-inbox text-4xl mb-3 text-slate-300"></i>
                    <p class="font-medium text-slate-600">Aucune demande d'expertise trouvée.</p>
                    <p class="text-xs text-slate-400 mt-1">Modifiez vos critères de filtrage ou attendez les sollicitations des généralistes.</p>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="d" items="${demandes}">
                    <div class="bg-white rounded-2xl border border-slate-200 shadow-sm p-6 space-y-4">
                        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 border-b border-slate-100 pb-3">
                            <div class="flex items-center space-x-3">
                                <h3 class="font-bold text-slate-900 text-base">Patient : ${d.consultation.patient.nomComplet}</h3>
                                <span class="px-2.5 py-0.5 rounded-full text-xs font-mono bg-slate-100 text-slate-700">
                                    ${d.consultation.patient.numSecu}
                                </span>
                            </div>
                            <div class="flex items-center space-x-2">
                                <span class="px-2.5 py-1 rounded-full text-xs font-bold
                                    <c:choose>
                                        <c:when test="${d.priorite == 'URGENTE'}">bg-red-100 text-red-800 border border-red-200</c:when>
                                        <c:when test="${d.priorite == 'NORMALE'}">bg-sky-100 text-sky-800 border border-sky-200</c:when>
                                        <c:otherwise>bg-slate-100 text-slate-700 border border-slate-200</c:otherwise>
                                    </c:choose>">
                                    Priorité : ${d.priorite.libelle}
                                </span>
                                <span class="px-2.5 py-1 rounded-full text-xs font-bold
                                    <c:choose>
                                        <c:when test="${d.statut == 'EN_ATTENTE'}">bg-amber-100 text-amber-800 border border-amber-200</c:when>
                                        <c:otherwise>bg-emerald-100 text-emerald-800 border border-emerald-200</c:otherwise>
                                    </c:choose>">
                                    ${d.statut.libelle}
                                </span>
                            </div>
                        </div>

                        <!-- Details row -->
                        <div class="grid grid-cols-1 md:grid-cols-3 gap-4 text-xs">
                            <div class="p-3 bg-slate-50 rounded-xl space-y-1">
                                <div class="font-semibold text-slate-700 uppercase">Médecin Généraliste :</div>
                                <div class="text-slate-900 font-medium">${d.consultation.generaliste.nomComplet}</div>
                                <div class="text-slate-500">Date demande : ${d.dateDemande.toString().substring(0, 16).replace('T', ' ')}</div>
                                <c:if test="${d.creneau != null}">
                                    <div class="text-purple-700 font-semibold pt-1">
                                        <i class="fa-regular fa-clock mr-1"></i> Créneau réservé : ${d.creneau.plageHoraire}
                                    </div>
                                </c:if>
                            </div>

                            <div class="p-3 bg-slate-50 rounded-xl space-y-1 md:col-span-2">
                                <div class="font-semibold text-slate-700 uppercase">Question & Données transmises :</div>
                                <p class="text-slate-900 font-medium">${d.question}</p>
                                <div class="text-slate-500 pt-1">
                                    <span class="font-semibold text-slate-600">Motif initial :</span> ${d.consultation.motif}
                                    &bull; <span class="font-semibold text-slate-600">Observations :</span> ${d.consultation.observations}
                                </div>
                            </div>
                        </div>

                        <!-- If already answered, show opinion, else show button to respond -->
                        <c:choose>
                            <c:when test="${not empty d.avisMedical}">
                                <div class="p-4 bg-emerald-50/70 border border-emerald-200 rounded-xl text-xs space-y-1.5">
                                    <div class="flex items-center justify-between text-emerald-900 font-bold">
                                        <span><i class="fa-solid fa-check-circle mr-1"></i> Avis Médical rendu le ${d.dateReponse.toString().substring(0, 16).replace('T', ' ')} :</span>
                                        <span class="text-emerald-700 text-[11px]">Expertise Clôturée</span>
                                    </div>
                                    <p class="text-slate-800">${d.avisMedical}</p>
                                    <c:if test="${not empty d.recommandations}">
                                        <p class="text-slate-600 pt-1"><strong>Recommandations :</strong> ${d.recommandations}</p>
                                    </c:if>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="flex justify-end pt-2">
                                    <a href="${pageContext.request.contextPath}/specialiste/repondre?id=${d.id}"
                                       class="px-5 py-2.5 bg-purple-600 hover:bg-purple-700 text-white rounded-xl text-xs font-bold shadow-sm transition flex items-center space-x-1.5">
                                        <i class="fa-solid fa-pen-to-square"></i>
                                        <span>US8 : Rédiger l'avis médical & Recommandations</span>
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
