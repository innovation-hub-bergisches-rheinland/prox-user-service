{{/*
Expand the name of the chart.
*/}}
{{- define "prox-user-service.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "prox-user-service.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "prox-user-service.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "prox-user-service.labels" -}}
helm.sh/chart: {{ include "prox-user-service.chart" . }}
{{ include "prox-user-service.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "prox-user-service.selectorLabels" -}}
app.kubernetes.io/name: {{ include "prox-user-service.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "prox-user-service.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "prox-user-service.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{/*
Looks if there's an existing secret and reuse its password. If not it generates a new password and uses it.
*/}}
{{- define "prox-user-service.database-user" -}}
{{- $secret := (lookup "v1" "Secret" .Release.Namespace (include "prox-user-service.fullname" .) ) -}}
  {{- if $secret -}}
    {{-  index $secret "data" "database-user" -}}
  {{- else -}}
    {{- "postgres" -}}
  {{- end -}}
{{- end -}}

{{/*
Looks if there's an existing secret and reuse its password. If not it generates a new password and uses it.
*/}}
{{- define "prox-user-service.database-password" -}}
{{- $secret := (lookup "v1" "Secret" .Release.Namespace (include "prox-user-service.fullname" .) ) -}}
  {{- if $secret -}}
    {{-  index $secret "data" "database-password" -}}
  {{- else -}}
    {{- (randAlphaNum 40) | b64enc | quote -}}
  {{- end -}}
{{- end -}}

{{/*
Looks if there's an existing secret and reuse its password. If not it generates a new password and uses it.
*/}}
{{- define "prox-user-service.keycloak.client-secret" -}}
{{- $secret := (lookup "v1" "Secret" .Release.Namespace (include "prox-user-service.fullname" .) ) -}}
  {{- if $secret -}}
    {{-  index $secret "data" "keycloak.client-secret" -}}
  {{- else -}}
    {{- uuidv4 | quote -}}
  {{- end -}}
{{- end -}}

{{/*
Looks if there's an existing secret and reuse its password. If not it generates a new password and uses it.
*/}}
{{- define "prox-user-service.replication-user-password" -}}
{{- $secret := (lookup "v1" "Secret" .Release.Namespace (include "prox-user-service.fullname" .) ) -}}
  {{- if $secret -}}
    {{-  index $secret "data" "replication-user-password" -}}
  {{- else -}}
    {{- (randAlphaNum 40) | b64enc | quote -}}
  {{- end -}}
{{- end -}}

{{/*
Looks if there's an existing secret and reuse its password. If not it generates a new password and uses it.
*/}}
{{- define "prox-user-service.aws.credentials.access-key-id" -}}
{{- $secret := (lookup "v1" "Secret" .Release.Namespace (include "prox-user-service.fullname" .) ) -}}
  {{- if $secret -}}
    {{-  index $secret "data" "aws.credentials.access-key-id" -}}
  {{- else -}}
    {{- (randAlphaNum 40) | b64enc | quote -}}
  {{- end -}}
{{- end -}}

{{/*
Looks if there's an existing secret and reuse its password. If not it generates a new password and uses it.
*/}}
{{- define "prox-user-service.aws.credentials.secret-access-key" -}}
{{- $secret := (lookup "v1" "Secret" .Release.Namespace (include "prox-user-service.fullname" .) ) -}}
  {{- if $secret -}}
    {{-  index $secret "data" "aws.credentials.secret-access-key" -}}
  {{- else -}}
    {{- (randAlphaNum 40) | b64enc | quote -}}
  {{- end -}}
{{- end -}}
