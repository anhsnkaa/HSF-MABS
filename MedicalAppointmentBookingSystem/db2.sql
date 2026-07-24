USE [master]
GO
/****** Object:  Database [MABS]    Script Date: 14/07/2026 13:47:54 ******/
CREATE DATABASE [MABS]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'MABS', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\MABS.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'MABS_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\MABS_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [MABS] SET COMPATIBILITY_LEVEL = 150
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [MABS].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [MABS] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [MABS] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [MABS] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [MABS] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [MABS] SET ARITHABORT OFF 
GO
ALTER DATABASE [MABS] SET AUTO_CLOSE ON 
GO
ALTER DATABASE [MABS] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [MABS] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [MABS] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [MABS] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [MABS] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [MABS] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [MABS] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [MABS] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [MABS] SET  ENABLE_BROKER 
GO
ALTER DATABASE [MABS] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [MABS] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [MABS] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [MABS] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [MABS] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [MABS] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [MABS] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [MABS] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [MABS] SET  MULTI_USER 
GO
ALTER DATABASE [MABS] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [MABS] SET DB_CHAINING OFF 
GO
ALTER DATABASE [MABS] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [MABS] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [MABS] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [MABS] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
ALTER DATABASE [MABS] SET QUERY_STORE = OFF
GO
USE [MABS]
GO
/****** Object:  Table [dbo].[account_status_log]    Script Date: 14/07/2026 13:47:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[account_status_log](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[user_id] [bigint] NOT NULL,
	[changed_by] [bigint] NOT NULL,
	[action] [varchar](20) NOT NULL,
	[reason] [nvarchar](500) NULL,
	[created_at] [datetime2](7) NOT NULL,
 CONSTRAINT [pk_account_status_log] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[appointment]    Script Date: 14/07/2026 13:47:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[appointment](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[patient_id] [bigint] NOT NULL,
	[doctor_id] [bigint] NOT NULL,
	[schedule_id] [bigint] NOT NULL,
	[appointment_time] [datetime2](0) NOT NULL,
	[status] [varchar](20) NOT NULL,
	[reason] [nvarchar](500) NULL,
	[cancel_reason] [nvarchar](500) NULL,
	[created_at] [datetime2](7) NOT NULL,
	[updated_at] [datetime2](7) NULL,
 CONSTRAINT [pk_appointment] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[doctor]    Script Date: 14/07/2026 13:47:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[doctor](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[user_id] [bigint] NOT NULL,
	[specialty_id] [bigint] NOT NULL,
	[title] [nvarchar](100) NULL,
	[bio] [nvarchar](max) NULL,
	[consultation_fee] [decimal](12, 0) NULL,
	[experience_years] [int] NULL,
 CONSTRAINT [pk_doctor] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[medical_record]    Script Date: 14/07/2026 13:47:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[medical_record](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[appointment_id] [bigint] NOT NULL,
	[doctor_id] [bigint] NOT NULL,
	[patient_id] [bigint] NOT NULL,
	[symptoms] [nvarchar](max) NULL,
	[diagnosis] [nvarchar](max) NOT NULL,
	[notes] [nvarchar](max) NULL,
	[visit_date] [date] NOT NULL,
	[created_at] [datetime2](7) NOT NULL,
 CONSTRAINT [pk_medical_record] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[medicine]    Script Date: 14/07/2026 13:47:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[medicine](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](150) NOT NULL,
	[unit] [nvarchar](50) NULL,
	[description] [nvarchar](max) NULL,
	[is_active] [bit] NOT NULL,
 CONSTRAINT [pk_medicine] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[notification]    Script Date: 14/07/2026 13:47:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[notification](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[user_id] [bigint] NOT NULL,
	[type] [varchar](30) NOT NULL,
	[message] [nvarchar](500) NOT NULL,
	[is_read] [bit] NOT NULL,
	[created_at] [datetime2](7) NOT NULL,
 CONSTRAINT [pk_notification] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[password_reset_tokens]    Script Date: 14/07/2026 13:47:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[password_reset_tokens](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[user_id] [bigint] NOT NULL,
	[token] [varchar](100) NOT NULL,
	[expiry_date] [datetime2](7) NOT NULL,
	[is_used] [bit] NOT NULL,
 CONSTRAINT [pk_password_reset_tokens] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[prescription]    Script Date: 14/07/2026 13:47:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[prescription](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[medical_record_id] [bigint] NOT NULL,
	[medicine_id] [bigint] NOT NULL,
	[quantity] [int] NOT NULL,
	[dosage] [nvarchar](100) NOT NULL,
	[frequency] [nvarchar](100) NOT NULL,
	[duration_days] [int] NOT NULL,
	[note] [nvarchar](255) NULL,
 CONSTRAINT [pk_prescription] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[review]    Script Date: 14/07/2026 13:47:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[review](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[appointment_id] [bigint] NOT NULL,
	[patient_id] [bigint] NOT NULL,
	[doctor_id] [bigint] NOT NULL,
	[rating] [int] NOT NULL,
	[comment] [nvarchar](1000) NULL,
	[created_at] [datetime2](7) NOT NULL,
 CONSTRAINT [pk_review] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[specialty]    Script Date: 14/07/2026 13:47:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[specialty](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](150) NOT NULL,
	[description] [nvarchar](max) NULL,
 CONSTRAINT [pk_specialty] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[test_result]    Script Date: 14/07/2026 13:47:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[test_result](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[appointment_id] [bigint] NOT NULL,
	[file_name] [nvarchar](255) NULL,
	[file_url] [varchar](500) NOT NULL,
	[file_type] [varchar](20) NULL,
	[uploaded_at] [datetime2](7) NOT NULL,
 CONSTRAINT [pk_test_result] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[users]    Script Date: 14/07/2026 13:47:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[users](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[full_name] [nvarchar](150) NOT NULL,
	[email] [varchar](150) NOT NULL,
	[phone] [varchar](20) NULL,
	[password_hash] [varchar](255) NOT NULL,
	[role] [varchar](20) NOT NULL,
	[status] [varchar](20) NOT NULL,
	[gender] [varchar](10) NULL,
	[date_of_birth] [date] NULL,
	[address] [nvarchar](255) NULL,
	[created_at] [datetime2](7) NOT NULL,
	[updated_at] [datetime2](7) NULL,
	[avatar_url] [varchar](500) NULL,
 CONSTRAINT [pk_users] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[working_schedule]    Script Date: 14/07/2026 13:47:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[working_schedule](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[doctor_id] [bigint] NOT NULL,
	[work_date] [date] NOT NULL,
	[start_time] [time](0) NOT NULL,
	[end_time] [time](0) NOT NULL,
	[slot_minutes] [int] NOT NULL,
	[status] [varchar](20) NOT NULL,
 CONSTRAINT [pk_working_schedule] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[appointment] ON 

INSERT [dbo].[appointment] ([id], [patient_id], [doctor_id], [schedule_id], [appointment_time], [status], [reason], [cancel_reason], [created_at], [updated_at]) VALUES (1, 4, 1, 1, CAST(N'2026-06-28T08:00:00.0000000' AS DateTime2), N'confirmed', N'Đau ngực, khó thở khi gắng sức', NULL, CAST(N'2026-06-28T06:05:56.9199186' AS DateTime2), NULL)
INSERT [dbo].[appointment] ([id], [patient_id], [doctor_id], [schedule_id], [appointment_time], [status], [reason], [cancel_reason], [created_at], [updated_at]) VALUES (2, 6, 1, 1, CAST(N'2026-07-01T01:48:00.0000000' AS DateTime2), N'cancelled', N'', N'cbm', CAST(N'2026-06-30T01:48:36.6076251' AS DateTime2), CAST(N'2026-06-30T02:05:39.4021489' AS DateTime2))
INSERT [dbo].[appointment] ([id], [patient_id], [doctor_id], [schedule_id], [appointment_time], [status], [reason], [cancel_reason], [created_at], [updated_at]) VALUES (3, 6, 1, 1, CAST(N'2026-07-15T22:42:00.0000000' AS DateTime2), N'confirmed', N'đau tim', NULL, CAST(N'2026-07-13T22:42:21.6161290' AS DateTime2), NULL)
INSERT [dbo].[appointment] ([id], [patient_id], [doctor_id], [schedule_id], [appointment_time], [status], [reason], [cancel_reason], [created_at], [updated_at]) VALUES (4, 6, 4, 10, CAST(N'2026-07-15T23:50:00.0000000' AS DateTime2), N'completed', N'nổi mụn', NULL, CAST(N'2026-07-13T23:50:50.8282680' AS DateTime2), CAST(N'2026-07-13T23:52:43.9582727' AS DateTime2))
SET IDENTITY_INSERT [dbo].[appointment] OFF
GO
SET IDENTITY_INSERT [dbo].[doctor] ON 

INSERT [dbo].[doctor] ([id], [user_id], [specialty_id], [title], [bio], [consultation_fee], [experience_years]) VALUES (1, 2, 1, N'ThS.BS', N'Chuyên gia tim mạch hơn 10 năm kinh nghiệm', CAST(300000 AS Decimal(12, 0)), 10)
INSERT [dbo].[doctor] ([id], [user_id], [specialty_id], [title], [bio], [consultation_fee], [experience_years]) VALUES (2, 3, 2, N'BS.CKI', N'Bác sĩ da liễu tận tâm', CAST(250000 AS Decimal(12, 0)), 7)
INSERT [dbo].[doctor] ([id], [user_id], [specialty_id], [title], [bio], [consultation_fee], [experience_years]) VALUES (3, 8, 5, N'lol', N'', NULL, NULL)
INSERT [dbo].[doctor] ([id], [user_id], [specialty_id], [title], [bio], [consultation_fee], [experience_years]) VALUES (4, 10, 2, N'ThS.BS', N'Chuyên gia tim mạch hơn 7 năm kinh nghiệm', CAST(200000 AS Decimal(12, 0)), 7)
SET IDENTITY_INSERT [dbo].[doctor] OFF
GO
SET IDENTITY_INSERT [dbo].[medical_record] ON 

INSERT [dbo].[medical_record] ([id], [appointment_id], [doctor_id], [patient_id], [symptoms], [diagnosis], [notes], [visit_date], [created_at]) VALUES (1, 1, 1, 4, N'Đau ngực, khó thở khi gắng sức', N'Thiếu máu cơ tim nhẹ, theo dõi huyết áp', N'Hạn chế vận động mạnh, tái khám sau 2 tuần', CAST(N'2026-06-28' AS Date), CAST(N'2026-06-28T06:05:56.9199186' AS DateTime2))
INSERT [dbo].[medical_record] ([id], [appointment_id], [doctor_id], [patient_id], [symptoms], [diagnosis], [notes], [visit_date], [created_at]) VALUES (2, 4, 4, 6, N'nổi mụn', N'bị dị ứng', N'uống thuốc đầy đủ', CAST(N'2026-07-13' AS Date), CAST(N'2026-07-13T23:52:43.9316177' AS DateTime2))
SET IDENTITY_INSERT [dbo].[medical_record] OFF
GO
SET IDENTITY_INSERT [dbo].[medicine] ON 

INSERT [dbo].[medicine] ([id], [name], [unit], [description], [is_active]) VALUES (1, N'Paracetamol 500mg', N'viên', N'Hạ sốt, giảm đau', 1)
INSERT [dbo].[medicine] ([id], [name], [unit], [description], [is_active]) VALUES (2, N'Amoxicillin 500mg', N'viên', N'Kháng sinh', 1)
INSERT [dbo].[medicine] ([id], [name], [unit], [description], [is_active]) VALUES (3, N'Vitamin C 1000mg', N'viên', N'Bổ sung vitamin C', 1)
INSERT [dbo].[medicine] ([id], [name], [unit], [description], [is_active]) VALUES (4, N'Loratadin 10mg', N'viên', N'Kháng histamin, chống dị ứng', 1)
SET IDENTITY_INSERT [dbo].[medicine] OFF
GO
SET IDENTITY_INSERT [dbo].[prescription] ON 

INSERT [dbo].[prescription] ([id], [medical_record_id], [medicine_id], [quantity], [dosage], [frequency], [duration_days], [note]) VALUES (1, 1, 1, 20, N'1 viên/lần', N'2 lần/ngày', 10, N'Uống sau ăn')
INSERT [dbo].[prescription] ([id], [medical_record_id], [medicine_id], [quantity], [dosage], [frequency], [duration_days], [note]) VALUES (2, 1, 3, 30, N'1 viên/lần', N'1 lần/ngày', 30, N'Uống buổi sáng')
INSERT [dbo].[prescription] ([id], [medical_record_id], [medicine_id], [quantity], [dosage], [frequency], [duration_days], [note]) VALUES (3, 2, 3, 10, N'1', N'2', 5, N'uống sau ăn')
INSERT [dbo].[prescription] ([id], [medical_record_id], [medicine_id], [quantity], [dosage], [frequency], [duration_days], [note]) VALUES (4, 2, 1, 5, N'2', N'1', 2, N'uống trước ăn 30 phút')
SET IDENTITY_INSERT [dbo].[prescription] OFF
GO
SET IDENTITY_INSERT [dbo].[specialty] ON 

INSERT [dbo].[specialty] ([id], [name], [description]) VALUES (1, N'Tim mạch', N'Khám và điều trị các bệnh về tim, mạch máu')
INSERT [dbo].[specialty] ([id], [name], [description]) VALUES (2, N'Da liễu', N'Khám và điều trị các bệnh về da')
INSERT [dbo].[specialty] ([id], [name], [description]) VALUES (3, N'Nhi khoa', N'Khám và điều trị cho trẻ em')
INSERT [dbo].[specialty] ([id], [name], [description]) VALUES (4, N'Thần kinh', N'Khám và điều trị các bệnh về thần kinh')
INSERT [dbo].[specialty] ([id], [name], [description]) VALUES (5, N'asdasasd', N'asdasdasdasdasd')
SET IDENTITY_INSERT [dbo].[specialty] OFF
GO
SET IDENTITY_INSERT [dbo].[users] ON 

INSERT [dbo].[users] ([id], [full_name], [email], [phone], [password_hash], [role], [status], [gender], [date_of_birth], [address], [created_at], [updated_at], [avatar_url]) VALUES (1, N'Quản trị viên', N'admin@mabs.vn', N'0900000000', N'$2b$10$y3rzRbhPJwYOFnD4a0z5t.5l2ei4v8/1DRmxYZkFBtHb2tR/wHp9u', N'admin', N'active', N'male', NULL, NULL, CAST(N'2026-06-28T06:05:56.9187012' AS DateTime2), NULL, NULL)
INSERT [dbo].[users] ([id], [full_name], [email], [phone], [password_hash], [role], [status], [gender], [date_of_birth], [address], [created_at], [updated_at], [avatar_url]) VALUES (2, N'BS. Nguyễn Văn An', N'an.doctor@mabs.vn', N'0900000001', N'$2b$10$y3rzRbhPJwYOFnD4a0z5t.5l2ei4v8/1DRmxYZkFBtHb2tR/wHp9u', N'doctor', N'active', N'male', NULL, NULL, CAST(N'2026-06-28T06:05:56.9189182' AS DateTime2), NULL, NULL)
INSERT [dbo].[users] ([id], [full_name], [email], [phone], [password_hash], [role], [status], [gender], [date_of_birth], [address], [created_at], [updated_at], [avatar_url]) VALUES (3, N'BS. Trần Thị Bình', N'binh.doctor@mabs.vn', N'0900000002', N'$2b$10$y3rzRbhPJwYOFnD4a0z5t.5l2ei4v8/1DRmxYZkFBtHb2tR/wHp9u', N'doctor', N'active', N'female', NULL, NULL, CAST(N'2026-06-28T06:05:56.9189182' AS DateTime2), NULL, NULL)
INSERT [dbo].[users] ([id], [full_name], [email], [phone], [password_hash], [role], [status], [gender], [date_of_birth], [address], [created_at], [updated_at], [avatar_url]) VALUES (4, N'Lê Thị Cẩm', N'cam.patient@mabs.vn', N'0911111111', N'$2b$10$y3rzRbhPJwYOFnD4a0z5t.5l2ei4v8/1DRmxYZkFBtHb2tR/wHp9u', N'patient', N'active', N'female', CAST(N'1995-04-20' AS Date), NULL, CAST(N'2026-06-28T06:05:56.9189182' AS DateTime2), NULL, NULL)
INSERT [dbo].[users] ([id], [full_name], [email], [phone], [password_hash], [role], [status], [gender], [date_of_birth], [address], [created_at], [updated_at], [avatar_url]) VALUES (5, N'Trần Gia Bảo', N'giabao_patient@gmail.com', NULL, N'$2a$10$K/SNPKobBmfhOEqFVl48beryPE0D1zhKUZ37BzlhEiuWcC5FMau7O', N'patient', N'active', NULL, NULL, NULL, CAST(N'2026-06-28T16:11:26.8706470' AS DateTime2), NULL, NULL)
INSERT [dbo].[users] ([id], [full_name], [email], [phone], [password_hash], [role], [status], [gender], [date_of_birth], [address], [created_at], [updated_at], [avatar_url]) VALUES (6, N'Nguyễn Thị Thúy Quỳnh', N'nt.thuyquynh1802@gmail.com', NULL, N'$2a$10$hSsX0LQU5Fc0SrBuNEyrMucO.mkWH9ZhAsd8bFvFwvqFjdNpLy74a', N'patient', N'active', NULL, NULL, NULL, CAST(N'2026-06-30T01:39:42.9103562' AS DateTime2), NULL, NULL)
INSERT [dbo].[users] ([id], [full_name], [email], [phone], [password_hash], [role], [status], [gender], [date_of_birth], [address], [created_at], [updated_at], [avatar_url]) VALUES (7, N'abc', N'test@gmail.com', NULL, N'$2a$10$ed7EIXCIUTqFdtlvUMaOd.qgq9khQ.ta7PT1QRkmuGXiBACaOBjsq', N'admin', N'active', NULL, NULL, NULL, CAST(N'2026-06-30T15:28:27.6012274' AS DateTime2), NULL, NULL)
INSERT [dbo].[users] ([id], [full_name], [email], [phone], [password_hash], [role], [status], [gender], [date_of_birth], [address], [created_at], [updated_at], [avatar_url]) VALUES (8, N'agc', N'test123@gmail.com', N'', N'', N'doctor', N'active', N'other', NULL, N'', CAST(N'2026-06-30T15:29:46.7315837' AS DateTime2), CAST(N'2026-06-30T15:30:40.1125997' AS DateTime2), N'')
INSERT [dbo].[users] ([id], [full_name], [email], [phone], [password_hash], [role], [status], [gender], [date_of_birth], [address], [created_at], [updated_at], [avatar_url]) VALUES (10, N'BS. Nguyễn Văn A', N'nguyenvana@gmail.com', N'0912354686', N'$2y$10$fpS05f.bwa5E5oMZGTELk.bHouN65AwEaNQsA..0ms3c9kezT8RRu', N'doctor', N'active', N'male', CAST(N'1997-02-13' AS Date), NULL, CAST(N'2026-07-13T15:53:28.9257505' AS DateTime2), NULL, NULL)
SET IDENTITY_INSERT [dbo].[users] OFF
GO
SET IDENTITY_INSERT [dbo].[working_schedule] ON 

INSERT [dbo].[working_schedule] ([id], [doctor_id], [work_date], [start_time], [end_time], [slot_minutes], [status]) VALUES (1, 1, CAST(N'2026-06-28' AS Date), CAST(N'08:00:00' AS Time), CAST(N'11:30:00' AS Time), 30, N'open')
INSERT [dbo].[working_schedule] ([id], [doctor_id], [work_date], [start_time], [end_time], [slot_minutes], [status]) VALUES (2, 1, CAST(N'2026-06-29' AS Date), CAST(N'08:00:00' AS Time), CAST(N'11:30:00' AS Time), 30, N'open')
INSERT [dbo].[working_schedule] ([id], [doctor_id], [work_date], [start_time], [end_time], [slot_minutes], [status]) VALUES (3, 2, CAST(N'2026-06-28' AS Date), CAST(N'13:30:00' AS Time), CAST(N'17:00:00' AS Time), 30, N'open')
INSERT [dbo].[working_schedule] ([id], [doctor_id], [work_date], [start_time], [end_time], [slot_minutes], [status]) VALUES (9, 4, CAST(N'2026-07-13' AS Date), CAST(N'08:00:00' AS Time), CAST(N'17:00:00' AS Time), 30, N'open')
INSERT [dbo].[working_schedule] ([id], [doctor_id], [work_date], [start_time], [end_time], [slot_minutes], [status]) VALUES (10, 4, CAST(N'2026-07-15' AS Date), CAST(N'08:00:00' AS Time), CAST(N'17:00:00' AS Time), 30, N'open')
SET IDENTITY_INSERT [dbo].[working_schedule] OFF
GO
/****** Object:  Index [ix_appointment_doctor]    Script Date: 14/07/2026 13:47:55 ******/
CREATE NONCLUSTERED INDEX [ix_appointment_doctor] ON [dbo].[appointment]
(
	[doctor_id] ASC,
	[appointment_time] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [ix_appointment_patient]    Script Date: 14/07/2026 13:47:55 ******/
CREATE NONCLUSTERED INDEX [ix_appointment_patient] ON [dbo].[appointment]
(
	[patient_id] ASC,
	[appointment_time] DESC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [ix_appointment_schedule]    Script Date: 14/07/2026 13:47:55 ******/
CREATE NONCLUSTERED INDEX [ix_appointment_schedule] ON [dbo].[appointment]
(
	[schedule_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [uq_appt_doctor_active_slot]    Script Date: 14/07/2026 13:47:55 ******/
CREATE UNIQUE NONCLUSTERED INDEX [uq_appt_doctor_active_slot] ON [dbo].[appointment]
(
	[doctor_id] ASC,
	[appointment_time] ASC
)
WHERE ([status] IN ('pending', 'confirmed'))
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [uq_doctor_user]    Script Date: 14/07/2026 13:47:55 ******/
ALTER TABLE [dbo].[doctor] ADD  CONSTRAINT [uq_doctor_user] UNIQUE NONCLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [ix_doctor_specialty]    Script Date: 14/07/2026 13:47:55 ******/
CREATE NONCLUSTERED INDEX [ix_doctor_specialty] ON [dbo].[doctor]
(
	[specialty_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [uq_mr_appointment]    Script Date: 14/07/2026 13:47:55 ******/
ALTER TABLE [dbo].[medical_record] ADD  CONSTRAINT [uq_mr_appointment] UNIQUE NONCLUSTERED 
(
	[appointment_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [ix_record_patient]    Script Date: 14/07/2026 13:47:55 ******/
CREATE NONCLUSTERED INDEX [ix_record_patient] ON [dbo].[medical_record]
(
	[patient_id] ASC,
	[visit_date] DESC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [uq_medicine_name]    Script Date: 14/07/2026 13:47:55 ******/
ALTER TABLE [dbo].[medicine] ADD  CONSTRAINT [uq_medicine_name] UNIQUE NONCLUSTERED 
(
	[name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [ix_notification_user_read]    Script Date: 14/07/2026 13:47:55 ******/
CREATE NONCLUSTERED INDEX [ix_notification_user_read] ON [dbo].[notification]
(
	[user_id] ASC,
	[is_read] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [uq_prt_token]    Script Date: 14/07/2026 13:47:55 ******/
ALTER TABLE [dbo].[password_reset_tokens] ADD  CONSTRAINT [uq_prt_token] UNIQUE NONCLUSTERED 
(
	[token] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [ix_prescription_record]    Script Date: 14/07/2026 13:47:55 ******/
CREATE NONCLUSTERED INDEX [ix_prescription_record] ON [dbo].[prescription]
(
	[medical_record_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [uq_review_appointment]    Script Date: 14/07/2026 13:47:55 ******/
ALTER TABLE [dbo].[review] ADD  CONSTRAINT [uq_review_appointment] UNIQUE NONCLUSTERED 
(
	[appointment_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [ix_review_doctor]    Script Date: 14/07/2026 13:47:55 ******/
CREATE NONCLUSTERED INDEX [ix_review_doctor] ON [dbo].[review]
(
	[doctor_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [uq_specialty_name]    Script Date: 14/07/2026 13:47:55 ******/
ALTER TABLE [dbo].[specialty] ADD  CONSTRAINT [uq_specialty_name] UNIQUE NONCLUSTERED 
(
	[name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [uq_users_email]    Script Date: 14/07/2026 13:47:55 ******/
ALTER TABLE [dbo].[users] ADD  CONSTRAINT [uq_users_email] UNIQUE NONCLUSTERED 
(
	[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [uq_ws_doctor_slot]    Script Date: 14/07/2026 13:47:55 ******/
ALTER TABLE [dbo].[working_schedule] ADD  CONSTRAINT [uq_ws_doctor_slot] UNIQUE NONCLUSTERED 
(
	[doctor_id] ASC,
	[work_date] ASC,
	[start_time] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [ix_schedule_doctor_date]    Script Date: 14/07/2026 13:47:55 ******/
CREATE NONCLUSTERED INDEX [ix_schedule_doctor_date] ON [dbo].[working_schedule]
(
	[doctor_id] ASC,
	[work_date] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[account_status_log] ADD  CONSTRAINT [df_asl_created]  DEFAULT (sysutcdatetime()) FOR [created_at]
GO
ALTER TABLE [dbo].[appointment] ADD  CONSTRAINT [df_appt_status]  DEFAULT ('pending') FOR [status]
GO
ALTER TABLE [dbo].[appointment] ADD  CONSTRAINT [df_appt_created]  DEFAULT (sysutcdatetime()) FOR [created_at]
GO
ALTER TABLE [dbo].[doctor] ADD  CONSTRAINT [df_doctor_fee]  DEFAULT ((0)) FOR [consultation_fee]
GO
ALTER TABLE [dbo].[doctor] ADD  CONSTRAINT [df_doctor_exp]  DEFAULT ((0)) FOR [experience_years]
GO
ALTER TABLE [dbo].[medical_record] ADD  CONSTRAINT [df_mr_visit]  DEFAULT (CONVERT([date],sysutcdatetime())) FOR [visit_date]
GO
ALTER TABLE [dbo].[medical_record] ADD  CONSTRAINT [df_mr_created]  DEFAULT (sysutcdatetime()) FOR [created_at]
GO
ALTER TABLE [dbo].[medicine] ADD  CONSTRAINT [df_med_active]  DEFAULT ((1)) FOR [is_active]
GO
ALTER TABLE [dbo].[notification] ADD  CONSTRAINT [df_noti_read]  DEFAULT ((0)) FOR [is_read]
GO
ALTER TABLE [dbo].[notification] ADD  CONSTRAINT [df_noti_created]  DEFAULT (sysutcdatetime()) FOR [created_at]
GO
ALTER TABLE [dbo].[password_reset_tokens] ADD  CONSTRAINT [df_prt_used]  DEFAULT ((0)) FOR [is_used]
GO
ALTER TABLE [dbo].[prescription] ADD  CONSTRAINT [df_pre_qty]  DEFAULT ((1)) FOR [quantity]
GO
ALTER TABLE [dbo].[prescription] ADD  CONSTRAINT [df_pre_days]  DEFAULT ((1)) FOR [duration_days]
GO
ALTER TABLE [dbo].[review] ADD  CONSTRAINT [df_rev_created]  DEFAULT (sysutcdatetime()) FOR [created_at]
GO
ALTER TABLE [dbo].[test_result] ADD  CONSTRAINT [df_tr_uploaded]  DEFAULT (sysutcdatetime()) FOR [uploaded_at]
GO
ALTER TABLE [dbo].[users] ADD  CONSTRAINT [df_users_status]  DEFAULT ('active') FOR [status]
GO
ALTER TABLE [dbo].[users] ADD  CONSTRAINT [df_users_created]  DEFAULT (sysutcdatetime()) FOR [created_at]
GO
ALTER TABLE [dbo].[working_schedule] ADD  CONSTRAINT [df_ws_slot]  DEFAULT ((30)) FOR [slot_minutes]
GO
ALTER TABLE [dbo].[working_schedule] ADD  CONSTRAINT [df_ws_status]  DEFAULT ('open') FOR [status]
GO
ALTER TABLE [dbo].[account_status_log]  WITH CHECK ADD  CONSTRAINT [fk_statuslog_changedby] FOREIGN KEY([changed_by])
REFERENCES [dbo].[users] ([id])
GO
ALTER TABLE [dbo].[account_status_log] CHECK CONSTRAINT [fk_statuslog_changedby]
GO
ALTER TABLE [dbo].[account_status_log]  WITH CHECK ADD  CONSTRAINT [fk_statuslog_user] FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([id])
GO
ALTER TABLE [dbo].[account_status_log] CHECK CONSTRAINT [fk_statuslog_user]
GO
ALTER TABLE [dbo].[appointment]  WITH CHECK ADD  CONSTRAINT [fk_appointment_doctor] FOREIGN KEY([doctor_id])
REFERENCES [dbo].[doctor] ([id])
GO
ALTER TABLE [dbo].[appointment] CHECK CONSTRAINT [fk_appointment_doctor]
GO
ALTER TABLE [dbo].[appointment]  WITH CHECK ADD  CONSTRAINT [fk_appointment_patient] FOREIGN KEY([patient_id])
REFERENCES [dbo].[users] ([id])
GO
ALTER TABLE [dbo].[appointment] CHECK CONSTRAINT [fk_appointment_patient]
GO
ALTER TABLE [dbo].[appointment]  WITH CHECK ADD  CONSTRAINT [fk_appointment_schedule] FOREIGN KEY([schedule_id])
REFERENCES [dbo].[working_schedule] ([id])
GO
ALTER TABLE [dbo].[appointment] CHECK CONSTRAINT [fk_appointment_schedule]
GO
ALTER TABLE [dbo].[doctor]  WITH CHECK ADD  CONSTRAINT [fk_doctor_specialty] FOREIGN KEY([specialty_id])
REFERENCES [dbo].[specialty] ([id])
GO
ALTER TABLE [dbo].[doctor] CHECK CONSTRAINT [fk_doctor_specialty]
GO
ALTER TABLE [dbo].[doctor]  WITH CHECK ADD  CONSTRAINT [fk_doctor_user] FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[doctor] CHECK CONSTRAINT [fk_doctor_user]
GO
ALTER TABLE [dbo].[medical_record]  WITH CHECK ADD  CONSTRAINT [fk_record_appointment] FOREIGN KEY([appointment_id])
REFERENCES [dbo].[appointment] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[medical_record] CHECK CONSTRAINT [fk_record_appointment]
GO
ALTER TABLE [dbo].[medical_record]  WITH CHECK ADD  CONSTRAINT [fk_record_doctor] FOREIGN KEY([doctor_id])
REFERENCES [dbo].[doctor] ([id])
GO
ALTER TABLE [dbo].[medical_record] CHECK CONSTRAINT [fk_record_doctor]
GO
ALTER TABLE [dbo].[medical_record]  WITH CHECK ADD  CONSTRAINT [fk_record_patient] FOREIGN KEY([patient_id])
REFERENCES [dbo].[users] ([id])
GO
ALTER TABLE [dbo].[medical_record] CHECK CONSTRAINT [fk_record_patient]
GO
ALTER TABLE [dbo].[notification]  WITH CHECK ADD  CONSTRAINT [fk_notification_user] FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[notification] CHECK CONSTRAINT [fk_notification_user]
GO
ALTER TABLE [dbo].[password_reset_tokens]  WITH CHECK ADD  CONSTRAINT [fk_prt_user] FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[password_reset_tokens] CHECK CONSTRAINT [fk_prt_user]
GO
ALTER TABLE [dbo].[prescription]  WITH CHECK ADD  CONSTRAINT [fk_prescription_medicine] FOREIGN KEY([medicine_id])
REFERENCES [dbo].[medicine] ([id])
GO
ALTER TABLE [dbo].[prescription] CHECK CONSTRAINT [fk_prescription_medicine]
GO
ALTER TABLE [dbo].[prescription]  WITH CHECK ADD  CONSTRAINT [fk_prescription_record] FOREIGN KEY([medical_record_id])
REFERENCES [dbo].[medical_record] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[prescription] CHECK CONSTRAINT [fk_prescription_record]
GO
ALTER TABLE [dbo].[review]  WITH CHECK ADD  CONSTRAINT [fk_review_appointment] FOREIGN KEY([appointment_id])
REFERENCES [dbo].[appointment] ([id])
GO
ALTER TABLE [dbo].[review] CHECK CONSTRAINT [fk_review_appointment]
GO
ALTER TABLE [dbo].[review]  WITH CHECK ADD  CONSTRAINT [fk_review_doctor] FOREIGN KEY([doctor_id])
REFERENCES [dbo].[doctor] ([id])
GO
ALTER TABLE [dbo].[review] CHECK CONSTRAINT [fk_review_doctor]
GO
ALTER TABLE [dbo].[review]  WITH CHECK ADD  CONSTRAINT [fk_review_patient] FOREIGN KEY([patient_id])
REFERENCES [dbo].[users] ([id])
GO
ALTER TABLE [dbo].[review] CHECK CONSTRAINT [fk_review_patient]
GO
ALTER TABLE [dbo].[test_result]  WITH CHECK ADD  CONSTRAINT [fk_testresult_appointment] FOREIGN KEY([appointment_id])
REFERENCES [dbo].[appointment] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[test_result] CHECK CONSTRAINT [fk_testresult_appointment]
GO
ALTER TABLE [dbo].[working_schedule]  WITH CHECK ADD  CONSTRAINT [fk_schedule_doctor] FOREIGN KEY([doctor_id])
REFERENCES [dbo].[doctor] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[working_schedule] CHECK CONSTRAINT [fk_schedule_doctor]
GO
ALTER TABLE [dbo].[account_status_log]  WITH CHECK ADD  CONSTRAINT [ck_asl_action] CHECK  (([action]='deactivate' OR [action]='activate' OR [action]='unlock' OR [action]='lock'))
GO
ALTER TABLE [dbo].[account_status_log] CHECK CONSTRAINT [ck_asl_action]
GO
ALTER TABLE [dbo].[appointment]  WITH CHECK ADD  CONSTRAINT [ck_appt_status] CHECK  (([status]='cancelled' OR [status]='completed' OR [status]='confirmed' OR [status]='pending'))
GO
ALTER TABLE [dbo].[appointment] CHECK CONSTRAINT [ck_appt_status]
GO
ALTER TABLE [dbo].[doctor]  WITH CHECK ADD  CONSTRAINT [ck_doctor_exp] CHECK  (([experience_years]>=(0)))
GO
ALTER TABLE [dbo].[doctor] CHECK CONSTRAINT [ck_doctor_exp]
GO
ALTER TABLE [dbo].[doctor]  WITH CHECK ADD  CONSTRAINT [ck_doctor_fee] CHECK  (([consultation_fee]>=(0)))
GO
ALTER TABLE [dbo].[doctor] CHECK CONSTRAINT [ck_doctor_fee]
GO
ALTER TABLE [dbo].[prescription]  WITH CHECK ADD  CONSTRAINT [ck_pre_days] CHECK  (([duration_days]>(0)))
GO
ALTER TABLE [dbo].[prescription] CHECK CONSTRAINT [ck_pre_days]
GO
ALTER TABLE [dbo].[prescription]  WITH CHECK ADD  CONSTRAINT [ck_pre_qty] CHECK  (([quantity]>(0)))
GO
ALTER TABLE [dbo].[prescription] CHECK CONSTRAINT [ck_pre_qty]
GO
ALTER TABLE [dbo].[review]  WITH CHECK ADD  CONSTRAINT [ck_review_rating] CHECK  (([rating]>=(1) AND [rating]<=(5)))
GO
ALTER TABLE [dbo].[review] CHECK CONSTRAINT [ck_review_rating]
GO
ALTER TABLE [dbo].[users]  WITH CHECK ADD  CONSTRAINT [ck_users_gender] CHECK  (([gender] IS NULL OR ([gender]='other' OR [gender]='female' OR [gender]='male')))
GO
ALTER TABLE [dbo].[users] CHECK CONSTRAINT [ck_users_gender]
GO
ALTER TABLE [dbo].[users]  WITH CHECK ADD  CONSTRAINT [ck_users_role] CHECK  (([role]='admin' OR [role]='doctor' OR [role]='patient'))
GO
ALTER TABLE [dbo].[users] CHECK CONSTRAINT [ck_users_role]
GO
ALTER TABLE [dbo].[users]  WITH CHECK ADD  CONSTRAINT [ck_users_status] CHECK  (([status]='locked' OR [status]='inactive' OR [status]='active'))
GO
ALTER TABLE [dbo].[users] CHECK CONSTRAINT [ck_users_status]
GO
ALTER TABLE [dbo].[working_schedule]  WITH CHECK ADD  CONSTRAINT [ck_ws_slot] CHECK  (([slot_minutes]>(0)))
GO
ALTER TABLE [dbo].[working_schedule] CHECK CONSTRAINT [ck_ws_slot]
GO
ALTER TABLE [dbo].[working_schedule]  WITH CHECK ADD  CONSTRAINT [ck_ws_status] CHECK  (([status]='cancelled' OR [status]='full' OR [status]='open'))
GO
ALTER TABLE [dbo].[working_schedule] CHECK CONSTRAINT [ck_ws_status]
GO
ALTER TABLE [dbo].[working_schedule]  WITH CHECK ADD  CONSTRAINT [ck_ws_time] CHECK  (([end_time]>[start_time]))
GO
ALTER TABLE [dbo].[working_schedule] CHECK CONSTRAINT [ck_ws_time]
GO
USE [master]
GO
ALTER DATABASE [MABS] SET  READ_WRITE 
GO
