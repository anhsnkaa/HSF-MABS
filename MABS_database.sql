/* ============================================================================
   MABS - Medical Appointment Booking System
   Database script for SQL Server (HSF302 - Spring Boot Web MVC)

   Actors      : Patient, Doctor, Admin
   Nghiệp vụ    : Bệnh nhân đặt lịch khám với bác sĩ -> bác sĩ ghi chẩn đoán,
                  kê đơn thuốc -> bệnh nhân xem hồ sơ bệnh án.

   Script này idempotent: chạy lại sẽ tạo mới database MABS từ đầu.
   ============================================================================ */

USE [master];
GO

/* ---- Tạo database (xóa nếu đã tồn tại để chạy lại sạch sẽ) ---------------- */
IF DB_ID(N'MABS') IS NOT NULL
BEGIN
    ALTER DATABASE [MABS] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE [MABS];
END
GO

CREATE DATABASE [MABS];
GO

ALTER DATABASE [MABS] SET COMPATIBILITY_LEVEL = 150;
GO
-- Bật READ_COMMITTED_SNAPSHOT để giảm khóa khi đọc (web đọc nhiều)
ALTER DATABASE [MABS] SET READ_COMMITTED_SNAPSHOT ON;
GO

USE [MABS];
GO

SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO

/* ============================================================================
   1. USERS - tài khoản chung cho cả 3 vai trò (patient / doctor / admin)
   ============================================================================ */
CREATE TABLE dbo.users
(
    id              UNIQUEIDENTIFIER NOT NULL CONSTRAINT df_users_id DEFAULT (NEWID()),
    full_name       NVARCHAR(150)    NOT NULL,
    email           VARCHAR(150)     NOT NULL,
    phone           VARCHAR(20)      NULL,
    password_hash   VARCHAR(255)     NOT NULL,
    role            VARCHAR(20)      NOT NULL,
    status          VARCHAR(20)      NOT NULL CONSTRAINT df_users_status DEFAULT ('active'),
    gender          VARCHAR(10)      NULL,
    date_of_birth   DATE             NULL,
    address         NVARCHAR(255)    NULL,
    created_at      DATETIME2(7)     NOT NULL CONSTRAINT df_users_created DEFAULT (SYSUTCDATETIME()),
    updated_at      DATETIME2(7)     NULL,
    CONSTRAINT pk_users PRIMARY KEY CLUSTERED (id),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT ck_users_role   CHECK (role IN ('patient', 'doctor', 'admin')),
    CONSTRAINT ck_users_status CHECK (status IN ('active', 'inactive', 'locked')),
    CONSTRAINT ck_users_gender CHECK (gender IS NULL OR gender IN ('male', 'female', 'other'))
);
GO

/* ============================================================================
   2. SPECIALTY - chuyên khoa (Tim mạch, Da liễu, ...)
   ============================================================================ */
CREATE TABLE dbo.specialty
(
    id          UNIQUEIDENTIFIER NOT NULL CONSTRAINT df_specialty_id DEFAULT (NEWID()),
    name        NVARCHAR(150)    NOT NULL,
    description NVARCHAR(MAX)    NULL,
    CONSTRAINT pk_specialty PRIMARY KEY CLUSTERED (id),
    CONSTRAINT uq_specialty_name UNIQUE (name)
);
GO

/* ============================================================================
   3. DOCTOR - hồ sơ bác sĩ (1-1 với users có role = 'doctor')
   ============================================================================ */
CREATE TABLE dbo.doctor
(
    id               UNIQUEIDENTIFIER NOT NULL CONSTRAINT df_doctor_id DEFAULT (NEWID()),
    user_id          UNIQUEIDENTIFIER NOT NULL,
    specialty_id     UNIQUEIDENTIFIER NOT NULL,
    title            NVARCHAR(100)    NULL,          -- học hàm/học vị: BS, ThS.BS, PGS.TS...
    bio              NVARCHAR(MAX)    NULL,
    consultation_fee DECIMAL(12, 0)   NOT NULL CONSTRAINT df_doctor_fee DEFAULT (0),
    rating_avg       DECIMAL(3, 2)    NOT NULL CONSTRAINT df_doctor_rating DEFAULT (0),
    rating_count     INT              NOT NULL CONSTRAINT df_doctor_rcount DEFAULT (0),
    experience_years INT              NOT NULL CONSTRAINT df_doctor_exp DEFAULT (0),
    CONSTRAINT pk_doctor PRIMARY KEY CLUSTERED (id),
    CONSTRAINT uq_doctor_user UNIQUE (user_id),
    CONSTRAINT ck_doctor_rating CHECK (rating_avg >= 0 AND rating_avg <= 5),
    CONSTRAINT ck_doctor_exp    CHECK (experience_years >= 0),
    CONSTRAINT ck_doctor_fee    CHECK (consultation_fee >= 0)
);
GO

/* ============================================================================
   4. WORKING_SCHEDULE - ca làm việc của bác sĩ (Admin gán ca - UC12)
   ============================================================================ */
CREATE TABLE dbo.working_schedule
(
    id          UNIQUEIDENTIFIER NOT NULL CONSTRAINT df_ws_id DEFAULT (NEWID()),
    doctor_id   UNIQUEIDENTIFIER NOT NULL,
    work_date   DATE             NOT NULL,
    start_time  TIME(0)          NOT NULL,
    end_time    TIME(0)          NOT NULL,
    slot_minutes INT             NOT NULL CONSTRAINT df_ws_slot DEFAULT (30), -- độ dài 1 lượt khám
    status      VARCHAR(20)      NOT NULL CONSTRAINT df_ws_status DEFAULT ('open'),
    CONSTRAINT pk_working_schedule PRIMARY KEY CLUSTERED (id),
    CONSTRAINT ck_ws_status CHECK (status IN ('open', 'full', 'cancelled')),
    CONSTRAINT ck_ws_time   CHECK (end_time > start_time),
    CONSTRAINT ck_ws_slot   CHECK (slot_minutes > 0),
    -- Một bác sĩ không thể có 2 ca trùng ngày + giờ bắt đầu
    CONSTRAINT uq_ws_doctor_slot UNIQUE (doctor_id, work_date, start_time)
);
GO

/* ============================================================================
   5. APPOINTMENT - lịch hẹn khám (UC03 đặt / UC04 hủy - đổi)
   ============================================================================ */
CREATE TABLE dbo.appointment
(
    id               UNIQUEIDENTIFIER NOT NULL CONSTRAINT df_appt_id DEFAULT (NEWID()),
    patient_id       UNIQUEIDENTIFIER NOT NULL,
    doctor_id        UNIQUEIDENTIFIER NOT NULL,
    schedule_id      UNIQUEIDENTIFIER NOT NULL,
    appointment_time DATETIME2(0)     NOT NULL,      -- thời điểm khám đã chọn trong ca
    status           VARCHAR(20)      NOT NULL CONSTRAINT df_appt_status DEFAULT ('pending'),
    reason           NVARCHAR(500)    NULL,          -- lý do khám / triệu chứng
    cancel_reason    NVARCHAR(500)    NULL,
    created_at       DATETIME2(7)     NOT NULL CONSTRAINT df_appt_created DEFAULT (SYSUTCDATETIME()),
    updated_at       DATETIME2(7)     NULL,
    CONSTRAINT pk_appointment PRIMARY KEY CLUSTERED (id),
    CONSTRAINT ck_appt_status CHECK (status IN ('pending', 'confirmed', 'completed', 'cancelled'))
);
GO

/* ============================================================================
   6. MEDICAL_RECORD - hồ sơ bệnh án (UC07 - 1 hồ sơ / 1 lần khám)
   ============================================================================ */
CREATE TABLE dbo.medical_record
(
    id             UNIQUEIDENTIFIER NOT NULL CONSTRAINT df_mr_id DEFAULT (NEWID()),
    appointment_id UNIQUEIDENTIFIER NOT NULL,
    doctor_id      UNIQUEIDENTIFIER NOT NULL,
    patient_id     UNIQUEIDENTIFIER NOT NULL,
    symptoms       NVARCHAR(MAX)    NULL,            -- triệu chứng
    diagnosis      NVARCHAR(MAX)    NOT NULL,        -- chẩn đoán
    notes          NVARCHAR(MAX)    NULL,            -- lời dặn của bác sĩ
    visit_date     DATE             NOT NULL CONSTRAINT df_mr_visit DEFAULT (CONVERT(DATE, SYSUTCDATETIME())),
    created_at     DATETIME2(7)     NOT NULL CONSTRAINT df_mr_created DEFAULT (SYSUTCDATETIME()),
    CONSTRAINT pk_medical_record PRIMARY KEY CLUSTERED (id),
    CONSTRAINT uq_mr_appointment UNIQUE (appointment_id)
);
GO

/* ============================================================================
   7. MEDICINE - danh mục thuốc
   ============================================================================ */
CREATE TABLE dbo.medicine
(
    id          UNIQUEIDENTIFIER NOT NULL CONSTRAINT df_med_id DEFAULT (NEWID()),
    name        NVARCHAR(150)    NOT NULL,
    unit        NVARCHAR(50)     NULL,               -- viên, vỉ, chai...
    description NVARCHAR(MAX)    NULL,
    is_active   BIT              NOT NULL CONSTRAINT df_med_active DEFAULT (1),
    CONSTRAINT pk_medicine PRIMARY KEY CLUSTERED (id),
    CONSTRAINT uq_medicine_name UNIQUE (name)
);
GO

/* ============================================================================
   8. PRESCRIPTION - đơn thuốc (UC08 - nhiều dòng thuốc / 1 hồ sơ)
   ============================================================================ */
CREATE TABLE dbo.prescription
(
    id                UNIQUEIDENTIFIER NOT NULL CONSTRAINT df_pre_id DEFAULT (NEWID()),
    medical_record_id UNIQUEIDENTIFIER NOT NULL,
    medicine_id       UNIQUEIDENTIFIER NOT NULL,
    quantity          INT              NOT NULL CONSTRAINT df_pre_qty DEFAULT (1),
    dosage            NVARCHAR(100)    NOT NULL,      -- liều: 1 viên/lần
    frequency         NVARCHAR(100)    NOT NULL,      -- tần suất: 2 lần/ngày
    duration_days     INT              NOT NULL CONSTRAINT df_pre_days DEFAULT (1),
    note              NVARCHAR(255)    NULL,          -- uống sau ăn...
    CONSTRAINT pk_prescription PRIMARY KEY CLUSTERED (id),
    CONSTRAINT ck_pre_qty  CHECK (quantity > 0),
    CONSTRAINT ck_pre_days CHECK (duration_days > 0)
);
GO

/* ============================================================================
   9. TEST_RESULT - kết quả xét nghiệm bệnh nhân tải lên (UC09)
   ============================================================================ */
CREATE TABLE dbo.test_result
(
    id             UNIQUEIDENTIFIER NOT NULL CONSTRAINT df_tr_id DEFAULT (NEWID()),
    appointment_id UNIQUEIDENTIFIER NOT NULL,
    file_name      NVARCHAR(255)    NULL,
    file_url       VARCHAR(500)     NOT NULL,
    file_type      VARCHAR(20)      NULL,            -- pdf, jpg, png...
    uploaded_at    DATETIME2(7)     NOT NULL CONSTRAINT df_tr_uploaded DEFAULT (SYSUTCDATETIME()),
    CONSTRAINT pk_test_result PRIMARY KEY CLUSTERED (id)
);
GO

/* ============================================================================
   10. REVIEW - đánh giá bác sĩ (1 đánh giá / 1 lần khám đã hoàn thành)
   ============================================================================ */
CREATE TABLE dbo.review
(
    id             UNIQUEIDENTIFIER NOT NULL CONSTRAINT df_rev_id DEFAULT (NEWID()),
    appointment_id UNIQUEIDENTIFIER NOT NULL,
    patient_id     UNIQUEIDENTIFIER NOT NULL,
    doctor_id      UNIQUEIDENTIFIER NOT NULL,
    rating         INT              NOT NULL,
    comment        NVARCHAR(1000)   NULL,
    created_at     DATETIME2(7)     NOT NULL CONSTRAINT df_rev_created DEFAULT (SYSUTCDATETIME()),
    CONSTRAINT pk_review PRIMARY KEY CLUSTERED (id),
    CONSTRAINT uq_review_appointment UNIQUE (appointment_id),
    CONSTRAINT ck_review_rating CHECK (rating BETWEEN 1 AND 5)
);
GO

/* ============================================================================
   11. NOTIFICATION - thông báo cho người dùng
   ============================================================================ */
CREATE TABLE dbo.notification
(
    id         UNIQUEIDENTIFIER NOT NULL CONSTRAINT df_noti_id DEFAULT (NEWID()),
    user_id    UNIQUEIDENTIFIER NOT NULL,
    type       VARCHAR(30)      NOT NULL,
    message    NVARCHAR(500)    NOT NULL,
    is_read    BIT              NOT NULL CONSTRAINT df_noti_read DEFAULT (0),
    created_at DATETIME2(7)     NOT NULL CONSTRAINT df_noti_created DEFAULT (SYSUTCDATETIME()),
    CONSTRAINT pk_notification PRIMARY KEY CLUSTERED (id)
);
GO

/* ============================================================================
   12. ACCOUNT_STATUS_LOG - lịch sử khóa/mở tài khoản (Admin thao tác)
   ============================================================================ */
CREATE TABLE dbo.account_status_log
(
    id         UNIQUEIDENTIFIER NOT NULL CONSTRAINT df_asl_id DEFAULT (NEWID()),
    user_id    UNIQUEIDENTIFIER NOT NULL,
    changed_by UNIQUEIDENTIFIER NOT NULL,
    action     VARCHAR(20)      NOT NULL,
    reason     NVARCHAR(500)    NULL,
    created_at DATETIME2(7)     NOT NULL CONSTRAINT df_asl_created DEFAULT (SYSUTCDATETIME()),
    CONSTRAINT pk_account_status_log PRIMARY KEY CLUSTERED (id),
    CONSTRAINT ck_asl_action CHECK (action IN ('lock', 'unlock', 'activate', 'deactivate'))
);
GO

/* ============================================================================
   FOREIGN KEYS
   Quy tắc ON DELETE:
   - Bảng phụ thuộc vòng đời cha (con sinh ra cùng cha) -> CASCADE.
   - Bảng tham chiếu danh mục / người dùng dùng để tra cứu -> NO ACTION
     (tránh multiple cascade paths mà SQL Server cấm, đồng thời giữ lịch sử).
   ============================================================================ */

-- doctor
ALTER TABLE dbo.doctor
    ADD CONSTRAINT fk_doctor_user FOREIGN KEY (user_id)
        REFERENCES dbo.users (id) ON DELETE CASCADE;
ALTER TABLE dbo.doctor
    ADD CONSTRAINT fk_doctor_specialty FOREIGN KEY (specialty_id)
        REFERENCES dbo.specialty (id);
GO

-- working_schedule
ALTER TABLE dbo.working_schedule
    ADD CONSTRAINT fk_schedule_doctor FOREIGN KEY (doctor_id)
        REFERENCES dbo.doctor (id) ON DELETE CASCADE;
GO

-- appointment  (nhiều cascade path -> để NO ACTION, xử lý xóa ở tầng ứng dụng)
ALTER TABLE dbo.appointment
    ADD CONSTRAINT fk_appointment_patient FOREIGN KEY (patient_id)
        REFERENCES dbo.users (id);
ALTER TABLE dbo.appointment
    ADD CONSTRAINT fk_appointment_doctor FOREIGN KEY (doctor_id)
        REFERENCES dbo.doctor (id);
ALTER TABLE dbo.appointment
    ADD CONSTRAINT fk_appointment_schedule FOREIGN KEY (schedule_id)
        REFERENCES dbo.working_schedule (id);
GO

-- medical_record
ALTER TABLE dbo.medical_record
    ADD CONSTRAINT fk_record_appointment FOREIGN KEY (appointment_id)
        REFERENCES dbo.appointment (id) ON DELETE CASCADE;
ALTER TABLE dbo.medical_record
    ADD CONSTRAINT fk_record_doctor FOREIGN KEY (doctor_id)
        REFERENCES dbo.doctor (id);
ALTER TABLE dbo.medical_record
    ADD CONSTRAINT fk_record_patient FOREIGN KEY (patient_id)
        REFERENCES dbo.users (id);
GO

-- prescription
ALTER TABLE dbo.prescription
    ADD CONSTRAINT fk_prescription_record FOREIGN KEY (medical_record_id)
        REFERENCES dbo.medical_record (id) ON DELETE CASCADE;
ALTER TABLE dbo.prescription
    ADD CONSTRAINT fk_prescription_medicine FOREIGN KEY (medicine_id)
        REFERENCES dbo.medicine (id);
GO

-- test_result
ALTER TABLE dbo.test_result
    ADD CONSTRAINT fk_testresult_appointment FOREIGN KEY (appointment_id)
        REFERENCES dbo.appointment (id) ON DELETE CASCADE;
GO

-- review
ALTER TABLE dbo.review
    ADD CONSTRAINT fk_review_appointment FOREIGN KEY (appointment_id)
        REFERENCES dbo.appointment (id);
ALTER TABLE dbo.review
    ADD CONSTRAINT fk_review_patient FOREIGN KEY (patient_id)
        REFERENCES dbo.users (id);
ALTER TABLE dbo.review
    ADD CONSTRAINT fk_review_doctor FOREIGN KEY (doctor_id)
        REFERENCES dbo.doctor (id);
GO

-- notification
ALTER TABLE dbo.notification
    ADD CONSTRAINT fk_notification_user FOREIGN KEY (user_id)
        REFERENCES dbo.users (id) ON DELETE CASCADE;
GO

-- account_status_log
ALTER TABLE dbo.account_status_log
    ADD CONSTRAINT fk_statuslog_user FOREIGN KEY (user_id)
        REFERENCES dbo.users (id);
ALTER TABLE dbo.account_status_log
    ADD CONSTRAINT fk_statuslog_changedby FOREIGN KEY (changed_by)
        REFERENCES dbo.users (id);
GO

/* ============================================================================
   RÀNG BUỘC NGHIỆP VỤ QUAN TRỌNG
   Một bác sĩ KHÔNG thể có 2 lịch hẹn ở cùng một thời điểm (trừ lịch đã hủy).
   Dùng filtered unique index để chống đặt trùng giờ (overbooking).
   ============================================================================ */
CREATE UNIQUE INDEX uq_appt_doctor_active_slot
    ON dbo.appointment (doctor_id, appointment_time)
    WHERE status IN ('pending', 'confirmed');
GO

/* ============================================================================
   INDEX phục vụ truy vấn thường dùng
   ============================================================================ */
CREATE INDEX ix_doctor_specialty       ON dbo.doctor (specialty_id);
CREATE INDEX ix_appointment_patient    ON dbo.appointment (patient_id, appointment_time DESC);
CREATE INDEX ix_appointment_doctor     ON dbo.appointment (doctor_id, appointment_time);
CREATE INDEX ix_appointment_schedule   ON dbo.appointment (schedule_id);
CREATE INDEX ix_schedule_doctor_date   ON dbo.working_schedule (doctor_id, work_date);
CREATE INDEX ix_record_patient         ON dbo.medical_record (patient_id, visit_date DESC);
CREATE INDEX ix_prescription_record    ON dbo.prescription (medical_record_id);
CREATE INDEX ix_review_doctor          ON dbo.review (doctor_id);
CREATE INDEX ix_notification_user_read ON dbo.notification (user_id, is_read);
GO

/* ============================================================================
   TRIGGER: tự cập nhật rating_avg / rating_count của bác sĩ khi có đánh giá
   ============================================================================ */
CREATE OR ALTER TRIGGER dbo.trg_review_update_rating
ON dbo.review
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;

    ;WITH affected AS (
        SELECT doctor_id FROM inserted
        UNION
        SELECT doctor_id FROM deleted
    )
    UPDATE d
    SET d.rating_count = ISNULL(r.cnt, 0),
        d.rating_avg   = ISNULL(r.avg_rating, 0)
    FROM dbo.doctor d
    INNER JOIN affected a ON a.doctor_id = d.id
    OUTER APPLY (
        SELECT COUNT(*)                       AS cnt,
               CAST(AVG(CAST(rating AS DECIMAL(5,2))) AS DECIMAL(3,2)) AS avg_rating
        FROM dbo.review rv
        WHERE rv.doctor_id = d.id
    ) r;
END;
GO

/* ============================================================================
   SEED DATA - dữ liệu mẫu để chạy thử ngay
   Mật khẩu của TẤT CẢ tài khoản mẫu: "admin123"
   (BCrypt hash, tương thích Spring Security BCryptPasswordEncoder)
   ============================================================================ */
DECLARE @pwd VARCHAR(255) = '$2b$10$y3rzRbhPJwYOFnD4a0z5t.5l2ei4v8/1DRmxYZkFBtHb2tR/wHp9u';

-- Chuyên khoa
DECLARE @sp_cardio UNIQUEIDENTIFIER = NEWID();
DECLARE @sp_derma  UNIQUEIDENTIFIER = NEWID();
DECLARE @sp_pedia  UNIQUEIDENTIFIER = NEWID();
DECLARE @sp_neuro  UNIQUEIDENTIFIER = NEWID();

INSERT INTO dbo.specialty (id, name, description) VALUES
    (@sp_cardio, N'Tim mạch',  N'Khám và điều trị các bệnh về tim, mạch máu'),
    (@sp_derma,  N'Da liễu',   N'Khám và điều trị các bệnh về da'),
    (@sp_pedia,  N'Nhi khoa',  N'Khám và điều trị cho trẻ em'),
    (@sp_neuro,  N'Thần kinh', N'Khám và điều trị các bệnh về thần kinh');

-- Admin
DECLARE @admin_id UNIQUEIDENTIFIER = NEWID();
INSERT INTO dbo.users (id, full_name, email, phone, password_hash, role, gender)
VALUES (@admin_id, N'Quản trị viên', 'admin@mabs.vn', '0900000000', @pwd, 'admin', 'male');

-- Bác sĩ (mỗi bác sĩ = 1 user role doctor + 1 dòng doctor)
DECLARE @u_doc1 UNIQUEIDENTIFIER = NEWID();
DECLARE @u_doc2 UNIQUEIDENTIFIER = NEWID();
DECLARE @doc1 UNIQUEIDENTIFIER = NEWID();
DECLARE @doc2 UNIQUEIDENTIFIER = NEWID();

INSERT INTO dbo.users (id, full_name, email, phone, password_hash, role, gender) VALUES
    (@u_doc1, N'BS. Nguyễn Văn An', 'an.doctor@mabs.vn', '0900000001', @pwd, 'doctor', 'male'),
    (@u_doc2, N'BS. Trần Thị Bình', 'binh.doctor@mabs.vn', '0900000002', @pwd, 'doctor', 'female');

INSERT INTO dbo.doctor (id, user_id, specialty_id, title, bio, consultation_fee, experience_years) VALUES
    (@doc1, @u_doc1, @sp_cardio, N'ThS.BS', N'Chuyên gia tim mạch hơn 10 năm kinh nghiệm', 300000, 10),
    (@doc2, @u_doc2, @sp_derma,  N'BS.CKI', N'Bác sĩ da liễu tận tâm', 250000, 7);

-- Bệnh nhân
DECLARE @pat1 UNIQUEIDENTIFIER = NEWID();
INSERT INTO dbo.users (id, full_name, email, phone, password_hash, role, gender, date_of_birth)
VALUES (@pat1, N'Lê Thị Cẩm', 'cam.patient@mabs.vn', '0911111111', @pwd, 'patient', 'female', '1995-04-20');

-- Ca làm việc mẫu cho bác sĩ 1 (hôm nay + ngày mai, sáng)
DECLARE @sched1 UNIQUEIDENTIFIER = NEWID();
INSERT INTO dbo.working_schedule (id, doctor_id, work_date, start_time, end_time, slot_minutes, status)
VALUES (@sched1, @doc1, CAST(SYSDATETIME() AS DATE), '08:00', '11:30', 30, 'open');

INSERT INTO dbo.working_schedule (id, doctor_id, work_date, start_time, end_time, slot_minutes, status) VALUES
    (NEWID(), @doc1, DATEADD(DAY, 1, CAST(SYSDATETIME() AS DATE)), '08:00', '11:30', 30, 'open'),
    (NEWID(), @doc2, CAST(SYSDATETIME() AS DATE), '13:30', '17:00', 30, 'open');

-- Danh mục thuốc
INSERT INTO dbo.medicine (name, unit, description) VALUES
    (N'Paracetamol 500mg', N'viên', N'Hạ sốt, giảm đau'),
    (N'Amoxicillin 500mg',  N'viên', N'Kháng sinh'),
    (N'Vitamin C 1000mg',   N'viên', N'Bổ sung vitamin C'),
    (N'Loratadin 10mg',     N'viên', N'Kháng histamin, chống dị ứng');

-- Một lịch hẹn mẫu (bệnh nhân đặt với bác sĩ 1, slot 08:00 hôm nay)
DECLARE @appt1 UNIQUEIDENTIFIER = NEWID();
INSERT INTO dbo.appointment (id, patient_id, doctor_id, schedule_id, appointment_time, status, reason)
VALUES (@appt1, @pat1, @doc1, @sched1,
        DATEADD(HOUR, 8, CAST(CAST(SYSDATETIME() AS DATE) AS DATETIME2(0))),
        'confirmed', N'Đau ngực, khó thở khi gắng sức');
GO

PRINT N'>>> MABS database đã tạo xong. Tài khoản mẫu mật khẩu: admin123';
GO
