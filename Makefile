# 로컬 개발용 명령어 모음

up:
	cd infra/docker && docker compose up -d

down:
	cd infra/docker && docker compose down

api:
	cd infra/docker && export $$(cat .env | xargs) && cd ../../apps/api && ./gradlew bootRun

api-clean:
	cd infra/docker && export $$(cat .env | xargs) && cd ../../apps/api && ./gradlew clean bootRun