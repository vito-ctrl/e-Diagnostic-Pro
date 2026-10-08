<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Mes Consultations - Médecin Généraliste" />
</jsp:include>

<div class="space-y-6">
    <div class="sm:flex sm:items-center sm:justify-between bg-white p-6 rounded-2xl border border-slate-200 shadow-sm">
        <div>
            <h1 class="text-2xl font-bold text-slate-900">Historique de mes consultations</h1>
            <p class="mt-1 text-sm text-slate-500">
                Suivi des consultations directes et des dossiers en télé-expertise
            </p>
        </div>
        <a href="${pageContext.request.contextPath}/generaliste/file-attente"
           class="px-4 py-2 bg-sky-600 hover:bg-sky-700 text-white rounded-xl text-sm font-semibold shadow-sm transition">
            <i class="fa-solid fa-list-ol mr-1"></i> File d'attente active
        </a>
    </div>

    <c:if test="${param.success == 'direct'}">
        <div class="p-4 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-800 text-sm flex items-center space-x-2">
            <i class="fa-solid fa-circle-check text-emerald-600 text-lg"></i>
            <span>Consultation enregistrée et clôturée avec succès en prise en charge directe !</span>
        </div>
    </c:if>

    <c:if test="${param.success == 'expertise'}">
        <div class="p-4 rounded-xl bg-purple-50 border border-purple-200 text-purple-800 text-sm flex items-center space-x-2">
            <i class="fa-solid fa-paper-plane text-purple-600 text-lg"></i>
            <span>Demande de télé-expertise transmise avec succès au médecin spécialiste (statut : EN_ATTENTE_AVIS_SPECIALISTE).</span>
        </div>
    </c:if>

    <div class="space-y-4">
        <c:choose>
            <c:when test="${empty consultations}">
                <div class="bg-white rounded-2xl border border-slate-200 p-12 text-center text-slate-400">
                    <i class="fa-solid fa-stethoscope text-4xl mb-3 text-slate-300"></i>
                    <p class="font-medium text-slate-600">Aucune consultation enregistrée pour l'instant.</p>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="c" items="${consultations}">
                    <div class="bg-white rounded-2xl border border-slate-200 shadow-sm p-6 space-y-4">
                        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2 border-b border-slate-100 pb-3">
                            <div class="flex items-center space-x-3">
                                <span class="font-bold text-slate-900 text-lg">${c.patient.nomComplet}</span>
                                <span class="px-2.5 py-0.5 rounded-full text-xs font-mono bg-slate-100 text-slate-700">
                                    ${c.patient.numSecu}
                                </span>
                            </div>
                            <div class="flex items-center space-x-3 text-xs">
                                <span class="text-slate-400">
                                    <i class="fa-regular fa-clock mr-1"></i> ${c.dateConsultation.toString().substring(0, 16).replace('T', ' ')}
                                </span>
                                <span class="px-3 py-1 rounded-full font-bold
                                    <c:choose>
                                        <c:when test="${c.statut == 'TERMINEE'}">bg-emerald-100 text-emerald-800 border border-emerald-200</c:when>
                                        <c:when test="${c.statut == 'EN_ATTENTE_AVIS_SPECIALISTE'}">bg-purple-100 text-purple-800 border border-purple-200</c:when>
                                        <c:otherwise>bg-sky-100 text-sky-800 border border-sky-200</c:otherwise>
                                    </c:choose>">
                                    ${c.statut.libelle}
                                </span>
                                <span class="px-3 py-1 bg-slate-900 text-white rounded-full font-mono font-bold">
                                    Total: ${c.coutTotal} DH
                                </span>
                            </div>
                        </div>

                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4 text-xs">
                            <div>
                                <span class="font-semibold text-slate-700 block uppercase mb-1">Motif & Observations :</span>
                                <p class="text-slate-600"><strong class="text-slate-800">Motif :</strong> ${c.motif}</p>
                                <p class="text-slate-600 mt-1"><strong class="text-slate-800">Observations :</strong> ${c.observations}</p>
                            </div>

                            <c:choose>
                                <c:when test="${not empty c.demandeExpertise}">
                                    <div class="p-3 bg-purple-50 rounded-xl border border-purple-100 space-y-2">
                                        <div class="flex items-center justify-between">
                                            <span class="font-bold text-purple-900 uppercase">Télé-Expertise</span>
                                            <span class="px-2 py-0.5 rounded-full text-[10px] font-bold 
                                                <c:choose>
                                                    <c:when test="${c.demandeExpertise.priorite == 'URGENTE'}">bg-red-100 text-red-800</c:when>
                                                    <c:otherwise>bg-slate-100 text-slate-800</c:otherwise>
                                                </c:choose>">
                                                Priorité : ${c.demandeExpertise.priorite.libelle}
                                            </span>
                                        </div>
                                        <div class="text-slate-700">
                                            <strong>Spécialiste sollicité :</strong> ${c.demandeExpertise.specialiste.nomComplet}
                                            (${c.demandeExpertise.specialiste.specialite.libelle})
                                        </div>
                                        <div class="text-slate-600">
                                            <strong>Question :</strong> ${c.demandeExpertise.question}
                                        </div>
                                        <c:if test="${not empty c.demandeExpertise.avisMedical}">
                                            <div class="mt-2 pt-2 border-t border-purple-200/60 bg-white p-2 rounded-lg">
                                                <div class="font-bold text-emerald-800"><i class="fa-solid fa-comment-medical mr-1"></i> Réponse du spécialiste :</div>
                                                <p class="text-slate-700 mt-0.5">${c.demandeExpertise.avisMedical}</p>
                                                <c:if test="${not empty c.demandeExpertise.recommandations}">
                                                    <p class="text-slate-600 mt-1"><em>Recommandations :</em> ${c.demandeExpertise.recommandations}</p>
                                                </c:if>
                                            </div>
                                        </c:if>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="p-3 bg-emerald-50 rounded-xl border border-emerald-100 space-y-2">
                                        <span class="font-bold text-emerald-900 uppercase block">Prise en charge directe</span>
                                        <p class="text-slate-700"><strong>Diagnostic :</strong> ${c.diagnostic}</p>
                                        <p class="text-slate-700"><strong>Prescription :</strong> <span class="font-mono">${c.prescription}</span></p>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <!-- Actes techniques badges -->
                        <c:if test="${not empty c.actesTechniques}">
                            <div class="pt-2 border-t border-slate-100 flex items-center space-x-2 text-xs">
                                <span class="text-slate-400 font-semibold">Actes réalisés :</span>
                                <div class="flex flex-wrap gap-1.5">
                                    <c:forEach var="act" items="${c.actesTechniques}">
                                        <span class="px-2 py-0.5 rounded-md bg-slate-100 text-slate-700 font-medium border border-slate-200">
                                            ${act.nom} (${act.tarif} DH)
                                        </span>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
